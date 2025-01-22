package kilowattcommando.controllerservice.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PushNotificationController {
    private SimpMessagingTemplate messagingTemplate;

    public PushNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(final String message) {
        this.messagingTemplate.convertAndSend("/powerplant", message);
    }
}
