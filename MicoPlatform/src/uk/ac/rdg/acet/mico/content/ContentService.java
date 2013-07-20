/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.content;

import uk.ac.rdg.acet.mico.Service;
import uk.ac.rdg.acet.mico.content.messages.ContentMessage;
import uk.ac.rdg.acet.mico.content.messages.ContentRequestMessage;
import uk.ac.rdg.acet.mico.content.messages.QueryMessage;
import uk.ac.rdg.acet.mico.content.messages.QueryResultMessage;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class ContentService extends Service {

    private ContentEventListener contentEventListener = null;

    public ContentService() {
        addMessageProcessor(ContentMessage.class.getName(), null);
        addMessageProcessor(ContentRequestMessage.class.getName(), null);
        addMessageProcessor(QueryMessage.class.getName(), null);
        addMessageProcessor(QueryResultMessage.class.getName(), null);
    }

    public void filterMessage(SimpleMessage message) {
        if (message instanceof ContentMessage) {

        }
        else
        if (message instanceof ContentRequestMessage) {

        }
        else
        if (message instanceof QueryMessage) {

        }
        else
        if (message instanceof QueryResultMessage) {

        }
    }

}
