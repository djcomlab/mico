/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.messages;

import com.sun.java.util.collections.Comparable;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;

/**
 *
 * @author David
 */
public class PartMessage extends SimpleMessage implements Comparable {

    private int sequenceNumber = -1;
    private int sequenceSize = -1;
    private byte[] data = null;
    private String parentMessageID = null;

    public PartMessage(String serviceID, String classID) {
        super(serviceID, classID);
    }

    public PartMessage(String destination, String parentMessageID, String serviceID, String classID, int sequenceNumber, int sequenceSize, byte[] data) {
        super(destination, serviceID, classID);
        this.parentMessageID = parentMessageID;
        this.sequenceNumber = sequenceNumber;
        this.sequenceSize = sequenceSize;
        this.data = data;
    }

    public String getParentMessageID() {
        return parentMessageID;
    }

    public void setParentMessageID(String parentMessageID) {
        this.parentMessageID = parentMessageID;
    }



    public byte[] getData() {
        return data;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getSequenceSize() {
        return sequenceSize;
    }

    public Message toJxtaMessage() {
        Message m = super.toJxtaMessage();
        StringMessageElement parentMessageIDElement = new StringMessageElement("parentMessageID", parentMessageID, null);
//        StringMessageElement parentTimestampElement = new StringMessageElement("parentTimestamp", String.valueOf(parentTimestamp), null);
        StringMessageElement sequenceNumberElement = new StringMessageElement("sequenceNumber", String.valueOf(sequenceNumber), null);
        StringMessageElement sequenceSizeElement = new StringMessageElement("sequenceSize", String.valueOf(sequenceSize), null);
        String dataString = new String(data);
        StringMessageElement dataElement = new StringMessageElement("data", dataString, null);
        String namespace = getServiceID(); // use service classname as namespace
        m.addMessageElement(namespace, parentMessageIDElement);
//        m.addMessageElement(namespace, parentTimestampElement);
        m.addMessageElement(namespace, sequenceNumberElement);
        m.addMessageElement(namespace, sequenceSizeElement);
        m.addMessageElement(namespace, dataElement);
        return m;
    }

    public void loadJxtaMessage(Message m) {
        super.loadJxtaMessage(m);
        String namespace = getServiceID(); // use service classname as namespace
        this.parentMessageID = m.getMessageElement(namespace, "parentMessageID").toString();
//        this.parentTimestamp = Long.parseLong(m.getMessageElement(namespace, "parentTimestamp").toString());
        this.sequenceNumber = Integer.parseInt(m.getMessageElement(namespace, "sequenceNumber").toString());
        this.sequenceSize = Integer.parseInt(m.getMessageElement(namespace, "sequenceSize").toString());
        this.data = m.getMessageElement(namespace, "data").getBytes(true);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( '{' );
        b.append(super.toString());
        b.append(',');
        b.append('{');
        b.append(this.sequenceNumber);
        b.append(',');
        b.append(this.sequenceSize);
        b.append(',');
        b.append(this.data.toString());
        b.append('}');
        b.append('}');
        return super.toString();
    }

    public int compareTo(Object obj) {
        PartMessage aPart = (PartMessage)obj;
        int comparison = 0;
        int thisSequenceNumber = this.sequenceNumber;
        int aPartSequenceNumber = aPart.getSequenceNumber();
        if (thisSequenceNumber>aPartSequenceNumber) comparison = 1;
        if (thisSequenceNumber<aPartSequenceNumber) comparison = -1;
        return comparison;
    }

}
