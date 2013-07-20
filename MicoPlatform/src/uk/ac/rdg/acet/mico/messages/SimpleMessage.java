/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.messages;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import uk.ac.rdg.acet.mico.Platform;
import uk.ac.rdg.acet.mico.SimpleMessagingService;

/**
 *
 * @author David
 */
public abstract class SimpleMessage implements IJxable {

    private String source = null;
    private String destination = null; // ignored for now
    private long timestamp = System.currentTimeMillis();
    private String messageID = null;
    private String serviceID = null;
    private String classID = null;

    /**
     * Constructs a default and uninitialized SimpleMessage object to use for deserializing
     * a raw JXTA Message received from by the {@link SimpleMessagingService}.
     * <p>
     * The objects values by default are all set to null.
     */
    public SimpleMessage(String serviceID, String classID) {
        super();
        this.serviceID = serviceID;
        this.classID = classID;
    }

    /**
     * Constructs an initialized SimpleMessage object to use for sending and serializing
     * into a JXTA Message to be sent via the {@link SimpleMessagingService}.
     *
     * @param destination The ID of the peer that the message is intended to be delivered to, null if to all in group.
     * @param serviceID The service identifier in order for sublcasses of SimpleMessage to be delegated to implementation specific message processors. Convention is to map to implementation class name.
     * @param classID The message class identifier, needed to de-jxtafy the messages
     */
    // remove source, and add a setter to do after construction of a message
    public SimpleMessage(String destination, String serviceID, String classID) {
        this.destination = destination;
        this.messageID = String.valueOf(timestamp);
        this.serviceID = serviceID;
        this.classID = classID;
    }

    public void setSource(String source) {
        this.source = source;
        int i = Platform.getRandom().nextInt(66-8); // random offset
        String sourcePartStr = source.substring(i+14, i+14+8); // 8 char random portion taken from source UUID
        this.messageID = sourcePartStr + timestamp; // append random portion to time
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setTimestamp(long timeStamp) {
        this.timestamp = timeStamp;
    }

    /**
     * Returns a JXTA transportable message that can be used by the SimpleMessagingService to send through the
     * broadcast pipe.
     *
     * @return A JXTA Message serialization of the SimpleMessage object.
     */
    public Message toJxtaMessage() {
        Message jxtaMessage = new Message();
        StringMessageElement senderElement = new StringMessageElement("source", this.source, null);
        StringMessageElement destinationElement = new StringMessageElement("destination", this.destination, null);
        StringMessageElement timestampElement = new StringMessageElement("timestamp", Long.toString(this.timestamp), null);
        StringMessageElement messageIDElement = new StringMessageElement("messageID", this.messageID, null);
        StringMessageElement serviceIDElement = new StringMessageElement("serviceID", this.serviceID, null);
        StringMessageElement classIDElement = new StringMessageElement("classID", this.classID, null);
        String namespace = this.serviceID; // use service classname as namespace
        jxtaMessage.addMessageElement(namespace, senderElement);
        jxtaMessage.addMessageElement(namespace, destinationElement);
        jxtaMessage.addMessageElement(namespace, timestampElement);
        jxtaMessage.addMessageElement(namespace, messageIDElement);
        jxtaMessage.addMessageElement(namespace, serviceIDElement);
        jxtaMessage.addMessageElement(namespace, classIDElement);
        return jxtaMessage;
    }

    /**
     * Used to deserialize a JXTA Message into a SimpleMessage object. Used by creating an uninitialized SimpleMessage
     * and then calling this method to initialize the state to correspond to the JXTA message provided.
     *
     * @param message The JXTA encoded message to deserialize.
     */
    public void loadJxtaMessage(Message jxtaMessage) {
        String namespace = this.serviceID; // use service classname as namespace
        this.source = jxtaMessage.getMessageElement(namespace, "source").toString();
        this.destination = jxtaMessage.getMessageElement(namespace, "destination").toString();
        this.timestamp = Long.parseLong(jxtaMessage.getMessageElement(namespace, "timestamp").toString());
        this.messageID = jxtaMessage.getMessageElement(namespace, "messageID").toString();
        this.serviceID = jxtaMessage.getMessageElement(namespace, "serviceID").toString();
        this.classID = jxtaMessage.getMessageElement(namespace, "classID").toString();
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( '{' );
        b.append( this.source );
        b.append( ',' );
        b.append(this.destination);
        b.append(',');
        b.append( this.timestamp );
        b.append( ',' );
        b.append( this.messageID );
        b.append( ',' );
        b.append( this.serviceID );
        b.append( ',' );
        b.append( this.classID );
        b.append( '}' );

        return b.toString();
    }

    public String getClassID() {
        return classID;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

}