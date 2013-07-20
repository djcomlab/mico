/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.messages;

import com.sun.java.util.collections.HashSet;
import com.sun.java.util.collections.Iterator;
import com.sun.java.util.collections.Set;
import com.sun.java.util.collections.SortedSet;
import com.sun.java.util.collections.TreeSet;
import net.jxta.endpoint.Message;

/**
 *
 * @author David
 */
public class MultipartMessage extends SimpleMessage {

    private SortedSet parts = new TreeSet();

    public MultipartMessage(String serviceID, String classID) {
        super(serviceID, classID);
    }

    public void loadJxtaMessages(Set jxtaMessages) {
        for (Iterator i = jxtaMessages.iterator();i.hasNext();) {
            Message jxtaMessage = (Message)i.next();
            PartMessage part = new PartMessage(getServiceID(), jxtaMessage.getMessageElement("classID").toString());
            part.loadJxtaMessage(jxtaMessage);
            setSource(part.getSource());
            setTimestamp(part.getTimestamp());
            setMessageID(part.getMessageID());
            parts.add(part);
        }
    }

    public Set toJxtaMessages() {
        HashSet jxtaMessages = new HashSet();
        for (Iterator i = parts.iterator();i.hasNext();) {
            PartMessage partMessage = (PartMessage)i.next();
            partMessage.setSource(getSource());
            jxtaMessages.add(partMessage.toJxtaMessage()); // test to check correct ordering of messages?
        }
        return jxtaMessages;
    }

    public MultipartMessage(String destination, String serviceID, String classID, byte[] data, int chunkSize) {
        super(destination, serviceID, classID);
        int numChunks = data.length / chunkSize;
        String parentMessageID = this.getMessageID();
        if ((data.length % chunkSize) > 0) {
            numChunks+=1;
            for (int i=1;i<numChunks;i++) {
                byte[] dataChunk = new byte[chunkSize];
                System.arraycopy(data, (i-1)*chunkSize, dataChunk, 0, chunkSize);
                PartMessage part = new PartMessage(destination, parentMessageID, serviceID, classID, i, numChunks, dataChunk);
                parts.add(part);
            }
            int bytesLeft = data.length-((numChunks-1)*chunkSize);
            byte[] dataChunk = new byte[bytesLeft];
            System.arraycopy(data, (numChunks-1)*chunkSize, dataChunk, 0, bytesLeft);
            PartMessage part = new PartMessage(destination, parentMessageID, serviceID, classID, numChunks, numChunks, dataChunk);
            parts.add(part);
        } else {
            for (int i=1;i<numChunks+1;i++) {
                byte[] dataChunk = new byte[chunkSize];
                System.arraycopy(data, (i-1)*chunkSize, dataChunk, 0, chunkSize);
                PartMessage part = new PartMessage(destination, parentMessageID, serviceID, classID, i, numChunks, dataChunk);
                parts.add(part);
            }
        }
    }

    public String getSource() {
        String source = super.getSource();
        if (source==null) {
            PartMessage firstPartMessage = (PartMessage)getParts().first();
            source = firstPartMessage.getSource();
        }
        return source;
    }

    public void setSource(String source) {
        super.setSource(source);
        for (Iterator i = getParts().iterator(); i.hasNext();) {
            PartMessage partMessage = (PartMessage)i.next();
            partMessage.setSource(source);
            partMessage.setParentMessageID(this.getMessageID());
        }
    }



    public SortedSet getParts() {
        return this.parts;
    }

    public byte[] getData() {
        String dataString = "";
        for (Iterator i = parts.iterator();i.hasNext();) {
            PartMessage part = (PartMessage)i.next();
            dataString = dataString + new String(part.getData());
        }
        return dataString.getBytes();
    }

    public int getDataSize() {
        int size = 0;
        if (parts.size()>1) {
            PartMessage firstPart = (PartMessage)parts.first(); // if parts > 1, first part is assumed equal to chunk size
            size = firstPart.getSequenceSize()*firstPart.getData().length; // number of chunks * chunk size
        }
        PartMessage lastPart = (PartMessage)parts.last(); // finally add the leftover bytes
        size = size + lastPart.getData().length;
        return size;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append('{');
        b.append( super.toString());
        b.append(',');
        b.append('{');
        b.append(getData().toString());
        b.append('}');
        b.append('}');
        return b.toString();
    }

}
