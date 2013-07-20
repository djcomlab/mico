/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.messages;

import com.sun.java.util.collections.Comparable;
import com.sun.java.util.collections.Iterator;
import com.sun.java.util.collections.SortedSet;
import com.sun.java.util.collections.TreeSet;
import external.util.ExpiringObjectTable;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import uk.ac.rdg.acet.mico.Platform;
import uk.ac.rdg.acet.mico.SimpleMessagingService;

/**
 *
 * @author David
 */
public abstract class MultipartMessageProcessor implements IMessageProcessor {

    final private ExpiringObjectTable incompleteMessageCache;

    public MultipartMessageProcessor() {
        Platform p = Platform.getInstance();
        SimpleMessagingService sms = (SimpleMessagingService)p.getService(SimpleMessagingService.class.getName());
        incompleteMessageCache = sms.getMessageCache();
    }

//    private final LRUCache incompleteMessageCache = new LRUCache(20); // arbitrarily 20 items in cache

    public SimpleMessage processMessage(Message jxtaMessage)  {
        MultipartMessage multipartMessage = null;
        String parentMessageID = jxtaMessage.getMessageElement("parentMessageID").toString();
        int sequenceSize = Integer.parseInt(jxtaMessage.getMessageElement("sequenceSize").toString());
        synchronized (incompleteMessageCache) {
            IncompleteMultipartMessage incompleteMessage = (IncompleteMultipartMessage)incompleteMessageCache.get(parentMessageID);
            if (incompleteMessage==null) {
                incompleteMessage =  new IncompleteMultipartMessage(sequenceSize, jxtaMessage);
                incompleteMessageCache.put(parentMessageID, incompleteMessage);
            } else {
                incompleteMessage.add(jxtaMessage);
                incompleteMessageCache.touch(parentMessageID);
            }
            if (incompleteMessage.isComplete()) {
                // pop a multipart message from the incomplete table here
                String source = jxtaMessage.getMessageElement("source").toString();
                String destination = jxtaMessage.getMessageElement("destination").toString();
                String serviceID = jxtaMessage.getMessageElement("serviceID").toString();
                String classID = jxtaMessage.getMessageElement("classID").toString();
                byte[] data = incompleteMessage.getData();
                int chunkSize = incompleteMessage.getChunkSize();
                multipartMessage = new MultipartMessage(destination, serviceID, classID, data, chunkSize);
                multipartMessage.setSource(source);
                multipartMessage.setMessageID(parentMessageID);
                incompleteMessageCache.remove(parentMessageID);
            }
        }
        return multipartMessage;
    }

    private class IncompleteMultipartMessage {
        private SortedSet incompleteMessageParts = new TreeSet();
        private int sequenceSize = -1;

        public IncompleteMultipartMessage(int sequenceSize, Message jxtaMessage) {
            this.sequenceSize = sequenceSize;
            incompleteMessageParts.add(new IncompleteMultipartMessagePart(jxtaMessage));
        }

        public void add(Message jxtaMessage) {
            IncompleteMultipartMessagePart part = new IncompleteMultipartMessagePart(jxtaMessage);
            if (!incompleteMessageParts.contains(part)) {
                incompleteMessageParts.add(new IncompleteMultipartMessagePart(jxtaMessage));
            }
        }

        public boolean isComplete() {
            return incompleteMessageParts.size() == sequenceSize;
        }

        public byte[] getData() {
            if (isComplete()) {
                String dataString = "";
                for (Iterator i = incompleteMessageParts.iterator();i.hasNext();) {
                    IncompleteMultipartMessagePart incompleteMessagePart = (IncompleteMultipartMessagePart)i.next();
                    Message jxtaMessage = incompleteMessagePart.getJxtaMessage();
                    dataString = dataString + jxtaMessage.getMessageElement("data").toString();
                }
                return dataString.getBytes();
            } else {
                return null;
            }
        }

        public int getDataSize() {
            return getData().length;
        }

        public int getChunkSize() {
            IncompleteMultipartMessagePart incompleteMultipartMessagePart = (IncompleteMultipartMessagePart)incompleteMessageParts.first();
            Message jxtaMessage = incompleteMultipartMessagePart.getJxtaMessage();
            MessageElement messageElement = jxtaMessage.getMessageElement("data");
            return messageElement.toString().length();
        }

        public int getSequenceSize() {
            return sequenceSize;
        }

    }

    private class IncompleteMultipartMessagePart implements Comparable {

        private int sequenceNumber = -1;
        private Message jxtaMessage = null;

        public IncompleteMultipartMessagePart(Message jxtaMessage) {
            this.jxtaMessage = jxtaMessage;
            this.sequenceNumber = Integer.parseInt(jxtaMessage.getMessageElement("sequenceNumber").toString());
        }

        public int compareTo(Object obj) {
            IncompleteMultipartMessagePart incompleteMessage = (IncompleteMultipartMessagePart)obj;
            int comparison = 0;
            int thisSequenceNumber = this.sequenceNumber;
            int incompleteMessageSequenceNumber = incompleteMessage.getSequenceNumber();
            if (thisSequenceNumber>incompleteMessageSequenceNumber) comparison = 1;
            if (thisSequenceNumber<incompleteMessageSequenceNumber) comparison = -1;
            return comparison;
        }

        public Message getJxtaMessage() {
            return jxtaMessage;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

    }
}
