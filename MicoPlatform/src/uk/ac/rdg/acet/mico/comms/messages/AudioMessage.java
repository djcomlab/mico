/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.comms.messages;

import uk.ac.rdg.acet.mico.comms.CommsService;
import uk.ac.rdg.acet.mico.messages.MultipartMessage;

/**
 *
 * @author David
 */
public class AudioMessage extends MultipartMessage {

    // TODO modify to allow definition of audio mime types (need to restrcuture MultipartMessage for this)
    public AudioMessage() {
        super(CommsService.class.getName(), AudioMessage.class.getName());
    }

    public AudioMessage(String destination, String messageID, byte[] data, int chunkSize) {
        super(destination, CommsService.class.getName(), AudioMessage.class.getName(), data, chunkSize);
    }

}
