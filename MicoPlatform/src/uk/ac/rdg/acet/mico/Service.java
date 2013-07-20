/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico;

import java.util.Hashtable;
import uk.ac.rdg.acet.mico.messages.IMessageProcessor;

/**
 *
 * @author David
 */
public abstract class Service implements IMessageFilter {

    private Hashtable messageProcessorRegistry = new Hashtable();

    protected void addMessageProcessor(String classID, IMessageProcessor messageProcessor) {
        messageProcessorRegistry.put(classID, messageProcessor);
    }

    protected IMessageProcessor getMessageProcessor(String classID) {
        IMessageProcessor messageProcessor = (IMessageProcessor)this.messageProcessorRegistry.get(classID);
        return messageProcessor;
    }

}
