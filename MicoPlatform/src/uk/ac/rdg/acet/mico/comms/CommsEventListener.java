/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.comms;

/**
 *
 * @author David
 */
public interface CommsEventListener {
    public void textMessageReceived(String sender, String textString);
    public void audioMessageReceived(String sender, byte[] audioData);
    public void videoMessageReceived(String sender, byte[] videoData);
}
