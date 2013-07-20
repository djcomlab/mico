/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.ident;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.jxta.endpoint.Message;
import net.jxta.impl.cm.LRUCache;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import uk.ac.rdg.acet.mico.Platform;
import uk.ac.rdg.acet.mico.Service;
import uk.ac.rdg.acet.mico.SimpleMessagingService;
import uk.ac.rdg.acet.mico.ident.messages.IdentEventMessage;
import uk.ac.rdg.acet.mico.messages.IMessageProcessor;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David Johnson
 */
public class IdentService extends Service {

    private final LRUCache peerIDMap = new LRUCache(32); // limit size, just so it's not unlimited
    private String localPeerID = Platform.getPeerID();
    private String localFriendlyName = localPeerID;
    private Timer timer = null;
    private static final Logger log = LoggerFactory.getLogger(IdentService.class);

    public IdentService() {
        // register IdentEventMessage processor
        addMessageProcessor(IdentEventMessage.class.getName(), new IMessageProcessor() {
            public SimpleMessage processMessage(Message jxtaMessage) {
                IdentEventMessage identEventMessage = new IdentEventMessage();
                identEventMessage.loadJxtaMessage(jxtaMessage);
                return identEventMessage;
            }
        });
        timer = new Timer();
        Platform platform = Platform.getInstance();
        SimpleMessagingService simpleMessagingService = (SimpleMessagingService)platform.getService(SimpleMessagingService.class.getName());
        timer.schedule(new PropagateIDTimerTask(simpleMessagingService), 0, 30*1000); // propagates every 30 seconds
    }

    /**
     * Takes messages returned by the message processor and filters and casts them
     * into a service specific message. On receipt of a IdentEventMessage this
     * method updates the internal peer ID map.
     *
     * @param message An instance of a SimpleMessage returned by a message processor.
     */
    public void filterMessage(SimpleMessage message) {
        if (message instanceof IdentEventMessage) {
            IdentEventMessage identEventMessage = (IdentEventMessage)message;
            String peerID = identEventMessage.getPeerID();
            String friendlyName = identEventMessage.getFriendlyName();
            // update local ID map
            synchronized (peerIDMap) {
                peerIDMap.put(peerID, friendlyName);
            }
        }
    }

    /**
     * Used to get the local friendly name of the user/peer, which should be set
     * by the user via an application interface.
     *
     * @return A human readable name of the local peer, as set by the user.
     */
    public String getLocalFriendlyName() {
        return localFriendlyName;
    }

    /**
     * Used to get the remote friendly name of the user/peer, which should be set
     * by the remote user via an application specific interface.
     *
     * @param peerID The JXTA UUID of a peer running the Mico platform.
     * @return A human readable name of the remote peer, as set by the remote user.
     */
    public String getRemoteFriendlyName(String peerID) {
        String friendlyName = null;
        synchronized (peerIDMap) {
            friendlyName = (String)peerIDMap.get(peerID);
        }
        if (friendlyName==null) {
            friendlyName = peerID;
        }
        return friendlyName;
    }

    /**
     * Used to set the local friendly name of the user/peer, which should be set
     * by the user via an application specific interface.
     *
     * @param localFriendlyName A human readable name of the local peer.
     */
    public void setLocalFriendlyName(String localFriendlyName) {
        this.localFriendlyName = localFriendlyName;
        peerIDMap.put(localPeerID, localFriendlyName);
    }

    private class PropagateIDTimerTask extends TimerTask {

        SimpleMessagingService simpleMessagingService = null;

        public PropagateIDTimerTask(SimpleMessagingService simpleMessagingService) {
            this.simpleMessagingService = simpleMessagingService;
        }

        public void run() {
            IdentEventMessage identEventMessage = new IdentEventMessage("", localPeerID, localFriendlyName);
            try {
                simpleMessagingService.send(identEventMessage);
            } catch (IOException ex) {
                log.error(ex);
            }
        }

    }

}
