/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.content.messages;

import uk.ac.rdg.acet.mico.messages.IJxable;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class ContentRequestMessage extends SimpleMessage implements IJxable {

    public ContentRequestMessage(String serviceID, String classID) {
        super(serviceID, classID);
    }

}
