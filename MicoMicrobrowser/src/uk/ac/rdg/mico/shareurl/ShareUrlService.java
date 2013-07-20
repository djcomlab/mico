/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.mico.shareurl;

import net.jxta.endpoint.Message;
import uk.ac.rdg.acet.mico.Service;
import uk.ac.rdg.acet.mico.messages.IMessageProcessor;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class ShareUrlService extends Service {

    private UrlChangeEventListener urlChangeEventListener = null;

    public ShareUrlService() {
        addMessageProcessor(UrlChangeMessage.class.getName(), new IMessageProcessor() {
            public SimpleMessage processMessage(Message jxtaMessage) {
                UrlChangeMessage urlUpdateMessage = new UrlChangeMessage();
                urlUpdateMessage.loadJxtaMessage(jxtaMessage);
                return urlUpdateMessage;
            }
        });
    }

    public void addEventListner(UrlChangeEventListener urlChangeEventListener) {
        this.urlChangeEventListener = urlChangeEventListener;
    }

    public void filterMessage(SimpleMessage message) {
        if (message instanceof UrlChangeMessage) {
            UrlChangeMessage urlUpdateMessage = (UrlChangeMessage)message;
            String url = urlUpdateMessage.getUrl();
            urlChangeEventListener.urlChangeEvent(url);
        }
    }

}
