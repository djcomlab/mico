/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.messages;

import net.jxta.endpoint.Message;

/**
 *
 * @author David
 */
public interface IMessageProcessor {
    public SimpleMessage processMessage(Message jxtaMessage);
}
