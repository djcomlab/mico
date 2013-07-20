/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico;

import com.sun.java.util.collections.Iterator;
import com.sun.java.util.collections.SortedSet;
import external.util.ExpiringObjectTable;
import external.util.LRUSet;
import java.io.IOException;
import java.util.Random;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.platform.Application;
import net.jxta.protocol.PipeAdvertisement;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import uk.ac.rdg.acet.mico.messages.IMessageProcessor;
import uk.ac.rdg.acet.mico.messages.MultipartMessage;
import uk.ac.rdg.acet.mico.messages.PartMessage;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David Johnson
 */
public final class SimpleMessagingService extends Service implements Application, PipeMsgListener {

    private final static String namespace = SimpleMessagingService.class.getName();
    private static final Logger log = LoggerFactory.getLogger(SimpleMessagingService.class);
    private PeerGroup group = null;
    private InputPipe inputPipe = null;
    private OutputPipe outputPipe = null;

    public int MODE_BROADCAST = -1;
    public int MODE_CS = 1;
    private int messagingMode = MODE_BROADCAST;
    public int MODE_BINARY_EXPONENTIAL = 0;
    public int MODE_POLYNOMIAL_BASE1 = 1;
    public int MODE_POLYNOMIAL_BASE2 = 2;
    public int MODE_POLYNOMIAL_BASE3 = 3;
    private int backoffMode = MODE_BINARY_EXPONENTIAL;
    private ExpiringObjectTable incompleteMessageCache = null; // arbitrarily 30s timeout;
    private static int MAX_QUEUE_LENGTH = 4096; // if max payload is assumed to be 64kb, this equates to a 32MB queue
    private long slotTime = 250;
    private final LRUSet receviedMessageIDCache = new LRUSet(MAX_QUEUE_LENGTH); // synchronize?
    private final SynchronizedSendFlag sendFlag = new SynchronizedSendFlag();
    private final SynchronizedRecvCounter recvCounter = new SynchronizedRecvCounter();
    private final SynchronizedRetryCounter retryCounter = new SynchronizedRetryCounter();

    public void filterMessage(SimpleMessage message) {
        // unused
    }

    // shared between all services
    public ExpiringObjectTable getMessageCache() {
        return incompleteMessageCache;
    }

    public void init(PeerGroup group, ID assignedID, Advertisement implAdv) throws PeerGroupException {
        this.group = group;
        log.debug(namespace + " initialized.");
    }

    public int startApp(String[] args) {
        // start listeners and handlers
        PipeService pipes = group.getPipeService();
        PipeAdvertisement pipeAdv = createPipeAdv(group);
        try {
            inputPipe = pipes.createInputPipe(pipeAdv, this);
            outputPipe = pipes.createOutputPipe(pipeAdv, 0);
        } catch (IOException ex) {
            log.error(ex);
        }  // create pipes and start listening on this
        if (args!=null) {
            if (args.length==3) {
                slotTime = Long.parseLong(args[0]);
                messagingMode = Integer.parseInt(args[1]);
                backoffMode = Integer.parseInt(args[2]);
            }
        }
        incompleteMessageCache = new ExpiringObjectTable(slotTime*128); // roughly max stall time (128 slots)
        log.debug(namespace + " started.");
        return 0;
    }

    public void setSlotTime(long slotTime) {
        this.slotTime = slotTime;
    }    

    private static int truncTwoPow(int i) {
        int[] twoPow = new int[] {1, 2, 4, 8, 16, 32, 64, 128}; // up to 2^8
        if (i>=8) {
            i = 8;
        }
        return twoPow[i];
    }

    private static int truncPolyBase1(int i) {
        int[] polyBase2 = new int[128]; 
        for (int j=0;j<128;j++) {
            polyBase2[j] = j+1;
        } // up to 128^1
        if (i>=8) {
            i = 8;
        }
        return polyBase2[i];
    }

    private static int truncPolyBase2(int i) {
        int[] polyBase2 = new int[] {1, 2, 9, 16, 25, 36, 49, 64, 128}; // up to 9^2
        if (i>=8) {
            i = 8;
        }
        return polyBase2[i];
    }

    private static int truncPolyBase3(int i) {
        int[] polyBase2 = new int[] {1, 8, 27, 64, 125}; // up to 5^3
        if (i>=8) {
            i = 8;
        }
        return polyBase2[i];
    }

    private void sendBroadcast(SimpleMessage message) {
        if (message!=null) {
            boolean isMessageSent = false;
            int numSendAttempts = 0;
            while (!isMessageSent) {
                try {
                    Message jxtaMessage = message.toJxtaMessage();
                    isMessageSent = outputPipe.send(jxtaMessage);
                } catch (IOException ex) {
                    log.error(ex);
                }
                // break if number of send attempts exceeds 32 tries.
                if (numSendAttempts++ > 32) {
                    log.error("Message send attempts exceeded 32 tries");
                    break;
                }
            }
            String messageID = message.getMessageID();
            log.debug("Sent;messageID=" + messageID);
        }
    }

    private void sendCS(SimpleMessage message) {
        if (message!=null) {
            boolean isMessageSent = false;
            int numSendAttempts = 0;
            while (!isMessageSent) {
                while(incompleteMessageCache.size()>0) {
                    // k = 2^{n}-1 where n is number of retries, where if n > 8, abort process
                    int kMax = 0;
                    switch(backoffMode) {
                        case 0: kMax = truncTwoPow(retryCounter.increment())-1; // k = 2^{n-1} where n is number of retries, and n > 8
                                break;
                        case 1: kMax = truncPolyBase1(retryCounter.increment())-1; // k = 1/(n^{1}) where n is number of retries, and n > 8
                                break;
                        case 2: kMax = truncPolyBase2(retryCounter.increment())-1; // k = 1/(n^{2}) where n is number of retries, and n > 8
                                break;
                        case 3: kMax = truncPolyBase3(retryCounter.increment())-1; // k = 1/(n^{3}) where n is number of retries, and n > 8
                                break;
                    }
                    int k = kMax;
                    if (kMax>0) {
                        k = new Random().nextInt(kMax);
                    }
                    log.warn("Outgoing send stalled for " + k + " slots; " + slotTime*k + "ms");
                    try {
                        Thread.sleep(slotTime * k);
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    }
                }
                try {
                    Message jxtaMessage = message.toJxtaMessage();
                    isMessageSent = outputPipe.send(jxtaMessage);
                } catch (IOException ex) {
                    log.error(ex);
                }
                // break if number of send attempts exceeds 32 tries.
                if (numSendAttempts++ > 32) {
                    log.error("Message send attempts exceeded 32 tries");
                    break;
                }
            }
            String messageID = message.getMessageID();
            log.debug("Sent;messageID=" + messageID);
        }
    }

    private PipeAdvertisement createPipeAdv(PeerGroup group) {
        PipeAdvertisement padv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        PipeID pipeId = IDFactory.newPipeID(group.getPeerGroupID(), namespace.getBytes());
        padv.setPipeID(pipeId);
        padv.setName(namespace + ".pipe");
        padv.setDescription("This pipe is for propagating all MessagingService messages within a group");
        padv.setType(PipeService.PropagateType);
        return padv;
    }

    public void stopApp() {
        if (inputPipe!=null)
            inputPipe.close();
        inputPipe = null;
        if (outputPipe!=null)
            outputPipe.close();
        outputPipe = null;
        incompleteMessageCache = null;
        log.debug(namespace + " stopped.");
        System.gc();
    }
    
    /**
     * Sends an instance of a {@link SimpleMessage} through the SimpleMessagingService
     * broadcast pipe. Messages extending a SimpleMessage can be sent through
     * this method. This method actually only adds messages to the outgoing message
     * queue as send rate is throttled to a constant max rate.
     * <p>
     * This method can send single part SimpleMessage objects, and also multipart
     * {@link MultipartMessage} objects.
     * 
     * @param message A SimpleMessage or extended class of a SimpleMessage
     * @throws IOException if the message can not be sent through the JXTA pipe
     */
    public void send(SimpleMessage message) throws IOException {
        String peerID = Platform.getPeerID();
        message.setSource(peerID);
        if (message instanceof MultipartMessage) {
            MultipartMessage multipartMessage = (MultipartMessage) message;
            multipartMessage.setSource(peerID);
            SortedSet parts = multipartMessage.getParts();
            for (Iterator i = parts.iterator(); i.hasNext();) {
                PartMessage partMessage = (PartMessage) i.next();
                switch (messagingMode) {
                    case 1: sendCS(partMessage);
                            break;
                    default: sendBroadcast(partMessage);
                }
            }
        } else {
            // do send single message
            switch (messagingMode) {
                case 1: sendCS(message);
                        break;
                default: sendBroadcast(message);                            
            }
        }
    }

    public void pipeMsgEvent(PipeMsgEvent event) {
        Message jxtaMessage = event.getMessage();
        // ignore message if source is self
        MessageElement sourceElement = jxtaMessage.getMessageElement("source");
        String source = sourceElement.toString();
        if (source==null) return;
        if (Platform.getPeerID().equals(source)) return;
        if (messagingMode==MODE_CS) {
            // collision detection here
            if (sendFlag.isSending()) {
                recvCounter.increment(); // increment number of recevied messages during send to indicate collision
            }
        }
        // check if message is a duplicate by looking at recently received message IDs
        MessageElement messageIDElement = jxtaMessage.getMessageElement("messageID");
        String messageID = messageIDElement.toString();
        if (receviedMessageIDCache.contains(messageID)) {
            log.debug("DuplRecv;messageID=" + messageID);
            return; // ignore if message was recently received
        } else {
            log.debug("Recv;messageID=" + messageID);
            receviedMessageIDCache.add(messageID);
        }
        Iterator namespaces = jxtaMessage.getMessageNamespaces();
        // delegate to appropriate message processors
        while(namespaces.hasNext()) {             
            String serviceID = (String)namespaces.next();
            Platform platform = Platform.getInstance();
            Service service = platform.getService(serviceID); // get service and send message to the service's message processor
            if (service!=null) {
                MessageElement classIDElement = jxtaMessage.getMessageElement(serviceID, "classID");
                String classID = classIDElement.toString();
                IMessageProcessor processor = service.getMessageProcessor(classID);
                SimpleMessage message = processor.processMessage(jxtaMessage);
                if (message!=null) {
                    service.filterMessage(message);
                }
                break; // break since message should only relate to a single service
            }
        }
    }

    private class SynchronizedRecvCounter {
        private int recvCount = 0;
        public synchronized int getRecvCount() {
            return recvCount;
        }
        public synchronized void increment() {
            this.recvCount++;
        }
        public synchronized void reset() {
            this.recvCount = 0;
        }
    }

    private class SynchronizedRetryCounter {
        private int retryCount = 0;
        public synchronized int getRetryCount() {
            return retryCount;
        }
        public synchronized int increment() {
            return this.retryCount++;
        }
        public synchronized void reset() {
            this.retryCount = 0;
        }
    }

    private class SynchronizedSendFlag {
        private boolean isSending = false;
        public synchronized boolean isSending() {
            return isSending;
        }
        public synchronized void setIsSending(boolean isSending) {
            this.isSending = isSending;
        }
    }

    private class SynchronizedRecvFlag {
        private boolean isReceiving = false;
        public synchronized boolean isReceiving() {
            return isReceiving;
        }
        public synchronized void setIsReceiving(boolean isReceiving) {
            this.isReceiving = isReceiving;
        }
    }

}
