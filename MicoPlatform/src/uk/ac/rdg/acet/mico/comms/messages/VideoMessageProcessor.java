package uk.ac.rdg.acet.mico.comms.messages;

import net.jxta.endpoint.Message;
import uk.ac.rdg.acet.mico.messages.MultipartMessage;
import uk.ac.rdg.acet.mico.messages.MultipartMessageProcessor;
import uk.ac.rdg.acet.mico.messages.SimpleMessage;

/**
 *
 * @author David
 */
public class VideoMessageProcessor extends MultipartMessageProcessor {

    public SimpleMessage processMessage(Message jxtaMessage) {
        VideoMessage videoMessage = null;
        MultipartMessage multiPartMessage = (MultipartMessage)super.processMessage(jxtaMessage);
        if (multiPartMessage != null) {
            videoMessage = new VideoMessage();
            videoMessage.loadJxtaMessages(multiPartMessage.toJxtaMessages());
        }
        return videoMessage;
    }

}
