package uk.ac.rdg.acet.mico.comms.messages;

import net.jxta.endpoint.Message;
import uk.ac.rdg.acet.mico.messages.MultipartMessage;
import uk.ac.rdg.acet.mico.messages.MultipartMessageProcessor;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class AudioMessageProcessor extends MultipartMessageProcessor {

    public SimpleMessage processMessage(Message jxtaMessage) {
        AudioMessage audioMessage = null;
        MultipartMessage multiPartMessage = (MultipartMessage)super.processMessage(jxtaMessage);
        if (multiPartMessage != null) {
            audioMessage = new AudioMessage();
            audioMessage.loadJxtaMessages(multiPartMessage.toJxtaMessages());
        }
        return audioMessage;
    }

}
