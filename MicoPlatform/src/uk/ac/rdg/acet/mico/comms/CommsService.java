/*
 * Copyright 2008-2010 David Johnson. All rights reserved.
 */

package uk.ac.rdg.acet.mico.comms;

import java.io.IOException;
import net.jxta.endpoint.Message;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import uk.ac.rdg.acet.mico.Platform;
import uk.ac.rdg.acet.mico.Service;
import uk.ac.rdg.acet.mico.SimpleMessagingService;
import uk.ac.rdg.acet.mico.comms.messages.AudioMessage;
import uk.ac.rdg.acet.mico.comms.messages.AudioMessageProcessor;
import uk.ac.rdg.acet.mico.comms.messages.TextMessage;
import uk.ac.rdg.acet.mico.comms.messages.VideoMessage;
import uk.ac.rdg.acet.mico.comms.messages.VideoMessageProcessor;
import uk.ac.rdg.acet.mico.messages.IMessageProcessor;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David Johnson
 */
public class CommsService extends Service {

    private CommsEventListener eventListener = null;
    private static final Logger log = LoggerFactory.getLogger(CommsService.class);
    private boolean isTestMode = false;
    private int chunkSize = 32 * 1024; // 32kb
    public CommsService() {
        // register TextMessage processor
        addMessageProcessor(TextMessage.class.getName(), new IMessageProcessor() {
            public SimpleMessage processMessage(Message jxtaMessage) {
                TextMessage textMessage = new TextMessage();
                textMessage.loadJxtaMessage(jxtaMessage);
                return textMessage;
            }
        });
        // register AudioMessage processor
        addMessageProcessor(AudioMessage.class.getName(), new AudioMessageProcessor() {
            public SimpleMessage processMessage(Message jxtaMessage) {
                SimpleMessage message = super.processMessage(jxtaMessage);
                if (message instanceof AudioMessage) {
                    return (AudioMessage)message;
                } else {
                    return null;
                }
            }
        });
        // register VideoMessage processor
        addMessageProcessor(VideoMessage.class.getName(), new VideoMessageProcessor() {
            public SimpleMessage processMessage(Message jxtaMessage) {
                SimpleMessage message = super.processMessage(jxtaMessage);
                if (message instanceof VideoMessage) {
                    return (VideoMessage)message;
                } else {
                    return null;
                }
            }
        });
    }

    public boolean isTestMode() {
        return this.isTestMode;
    }

    public void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /**
     * Sends a text message to all listening peers in a Mico group.
     *
     * @param text A textual message expressed as a String.
     */
    public void sendText(String text) {
        TextMessage textMessage = new TextMessage("", text);
        Platform platform = Platform.getInstance();
        SimpleMessagingService simpleMessagingService = (SimpleMessagingService)platform.getService(SimpleMessagingService.class.getName());
        try {
            simpleMessagingService.send(textMessage);
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    /**
     * Sends an audio media message to all listening peers in a Mico group.
     *
     * @param audioData A byte array containing audio data. Usually audio/wav
     */
    public void sendAudio(byte[] audioData) {
        // creates a new audio message using data, 1kb chunks
        AudioMessage audioMessage = new AudioMessage("", Long.toString(System.currentTimeMillis()), audioData, chunkSize);
        Platform platform = Platform.getInstance();
        SimpleMessagingService simpleMessagingService = (SimpleMessagingService)platform.getService(SimpleMessagingService.class.getName());
        try {
            simpleMessagingService.send(audioMessage);
        } catch (IOException ex) {
            log.error(ex);
        }
        if (isTestMode) {
            String logOut = "Sent;messageID=" + audioMessage.getMessageID()
                    + ";timestamp=" + audioMessage.getTimestamp()
                    + ";size=" + audioData.length;
            log.info(logOut);
        }
    }

    /**
     * Sends a video media message to all listening peers in a Mico group.
     *
     * @param videoData A byte array containing video data. Usually video/3gpp
     */
    public void sendVideo(byte[] videoData) {
        // creates a new audio message using data, 1kb chunks
        VideoMessage videoMessage = new VideoMessage("", Long.toString(System.currentTimeMillis()), videoData, chunkSize);
        Platform platform = Platform.getInstance();
        SimpleMessagingService simpleMessagingService = (SimpleMessagingService)platform.getService(SimpleMessagingService.class.getName());
        try {
            simpleMessagingService.send(videoMessage);
        } catch (IOException ex) {
            log.error(ex);
        }
        if (isTestMode) {
            String logOut = "Sent;messageID=" + videoMessage.getMessageID()
                    + ";timestamp=" + videoMessage.getTimestamp()
                    + ";size=" + videoData.length;
            log.info(logOut);
        }
    }

    /**
     * Registers an event listener with the CommsService to hook back into a user
     * application.
     *
     * @param eventListener An implementation of the CommsEventListener interface.
     */
    public void addEventListener(CommsEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Takes messages returned by the message processors and filters and casts them
     * into service specific messages, then pops them via the registered event listener.
     *
     * @param message An instance of a SimpleMessage returned by a message processor.
     */
    public void filterMessage(SimpleMessage message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)message;
            eventListener.textMessageReceived(textMessage.getSource(), textMessage.getText());
            if (isTestMode) {
                String messageID = textMessage.getMessageID();
                String source = textMessage.getSource();
                long timestamp = textMessage.getTimestamp();
                int dataLength = textMessage.getText().length();
                String logOut = "Received;messageID=" + messageID
                        + ";source=" + source
                        + ";timestamp=" + timestamp
                        + ";size=" + dataLength;
                log.info(logOut);
            }
        }
        else
        if (message instanceof AudioMessage) {
            AudioMessage audioMessage = (AudioMessage)message;
            String source = audioMessage.getSource();
            byte[] audioData = audioMessage.getData();
            eventListener.audioMessageReceived(source, audioData);
            if (isTestMode) {
                String messageID = audioMessage.getMessageID();
                //String source = audioMessage.getSource();
                long timestamp = audioMessage.getTimestamp();
                int dataLength = audioMessage.getData().length;
                String logOut = "Received;messageID=" + messageID
                        + ";source=" + source
                        + ";timestamp=" + timestamp
                        + ";size=" + dataLength;
                log.info(logOut);
            }
        }
        else
        if (message instanceof VideoMessage) {
            VideoMessage videoMessage = (VideoMessage)message;
            String source = videoMessage.getSource();
            byte[] videoData = videoMessage.getData();
            eventListener.videoMessageReceived(source, videoData);
            if (isTestMode) {
                String messageID = videoMessage.getMessageID();
                //String source = videoMessage.getSource();
                long timestamp = videoMessage.getTimestamp();
                int dataLength = videoMessage.getData().length;
                String logOut = "Received;messageID=" + messageID
                        + ";source=" + source
                        + ";timestamp=" + timestamp
                        + ";size=" + dataLength;
                log.info(logOut);
            }
        }
    }

}
