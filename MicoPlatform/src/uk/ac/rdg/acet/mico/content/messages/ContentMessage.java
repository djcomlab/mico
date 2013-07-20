/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.content.messages;

import uk.ac.rdg.acet.mico.messages.MultipartMessage;

/**
 *
 * @author David
 */
public class ContentMessage extends MultipartMessage {

    public ContentMessage(String serviceID, String classID) {
        super(serviceID, classID);
    }

}
