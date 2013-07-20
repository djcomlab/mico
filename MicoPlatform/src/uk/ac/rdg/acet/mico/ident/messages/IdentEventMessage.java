/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.ident.messages;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import uk.ac.rdg.acet.mico.ident.IdentService;
import uk.ac.rdg.acet.mico.messages.IJxable;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class IdentEventMessage extends SimpleMessage implements IJxable {

    private String peerID = null;
    private String friendlyName = null;

    public IdentEventMessage() {
        super(IdentService.class.getName(), IdentEventMessage.class.getName());
    }

    public IdentEventMessage(String destination, String peerID, String friendlyName) {
        super(destination, IdentService.class.getName(), IdentEventMessage.class.getName());
        this.peerID = peerID;
        this.friendlyName = friendlyName;
    }

    public void loadJxtaMessage(Message message) {
        super.loadJxtaMessage(message);
        String namespace = getServiceID(); // use service classname as namespace
        this.peerID = message.getMessageElement(namespace, "peerID").toString();
        this.friendlyName = message.getMessageElement(namespace, "friendlyName").toString();
    }

    public Message toJxtaMessage() {
        Message message = super.toJxtaMessage();
        StringMessageElement peerIDElement = new StringMessageElement("peerID", peerID, null);
        StringMessageElement friendlyNameElement = new StringMessageElement("friendlyName", friendlyName,  null);
        String namespace = getServiceID(); // use service classname as namespace
        message.addMessageElement(namespace, peerIDElement);
        message.addMessageElement(namespace, friendlyNameElement);
        return message;
    }

    public String toString() {
        return super.toString();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getPeerID() {
        return peerID;
    }

    

}
