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
public class VideoMessage extends MultipartMessage {

    // TODO modify to allow definition of video mime types (need to restrcuture MultipartMessage for this)
    public VideoMessage() {
        super(CommsService.class.getName(), VideoMessage.class.getName());
    }

    public VideoMessage(String destination, String messageID, byte[] data, int chunkSize) {
        super(destination, CommsService.class.getName(), VideoMessage.class.getName(), data, chunkSize);
    }

}
