/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.comms.messages;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import uk.ac.rdg.acet.mico.comms.CommsService;
import uk.ac.rdg.acet.mico.messages.IJxable;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class TextMessage extends SimpleMessage implements IJxable {

    private String text = null;

    public TextMessage(String destination, String text) {
        super(destination, CommsService.class.getName(), TextMessage.class.getName());
        this.text = text;
    }

    public TextMessage() {
        super(CommsService.class.getName(), TextMessage.class.getName());
    }

    public void loadJxtaMessage(Message jxtaMessage) {
        super.loadJxtaMessage(jxtaMessage);
        String namespace = getServiceID(); // use service classname as namespace
        this.text = jxtaMessage.getMessageElement(namespace, "text").toString();
    }

    public Message toJxtaMessage() {
        Message jxtaMessage = super.toJxtaMessage();
        StringMessageElement textElement = new StringMessageElement("text", text, null);
        String namespace = getServiceID(); // use service classname as namespace
        jxtaMessage.addMessageElement(namespace, textElement);
        return jxtaMessage;
    }

    public String toString() {
        return super.toString();
    }

    public String getText() {
        return text;
    }

}
