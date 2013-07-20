/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.mico.shareurl;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import uk.ac.rdg.acet.mico.messages.IJxable;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class UrlChangeMessage extends SimpleMessage implements IJxable {

    private String url = null;

    public UrlChangeMessage() {
        super(ShareUrlService.class.getName(), UrlChangeMessage.class.getName());
    }

    public UrlChangeMessage(String destination, String messageID, String url) {
        super(destination, ShareUrlService.class.getName(), UrlChangeMessage.class.getName());
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void loadJxtaMessage(Message jxtaMessage) {
        super.loadJxtaMessage(jxtaMessage);
        String namespace = getServiceID(); // use service classname as namespace
        this.url = jxtaMessage.getMessageElement(namespace, "url").toString();
    }

    public Message toJxtaMessage() {
        Message jxtaMessage = super.toJxtaMessage();
        StringMessageElement urlElement = new StringMessageElement("url", url, null);
        String namespace = getServiceID(); // use service classname as namespace
        jxtaMessage.addMessageElement(namespace, urlElement);
        return jxtaMessage;
    }

}
