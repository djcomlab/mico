/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico;

import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public interface IMessageFilter {
    public void filterMessage(SimpleMessage message);
}
