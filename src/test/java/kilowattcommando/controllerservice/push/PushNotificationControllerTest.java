package kilowattcommando.controllerservice.push;

import kilowattcommando.controllerservice.kafka.KafkaContainerExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
        })
@DirtiesContext
@ExtendWith(KafkaContainerExtension.class)
public class PushNotificationControllerTest {
    private static PushNotificationController pushNotificationController;

    private static SimpMessagingTemplate simpMessagingTemplate;

    @BeforeAll
    static void createPushNotificationController() {
        simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        pushNotificationController = new PushNotificationController(simpMessagingTemplate);
    }

    @Test
    void testPushNotification() {
        pushNotificationController.sendMessage("No missing powerplants");

        verify(simpMessagingTemplate).convertAndSend("/powerplant", "No missing powerplants");
    }
}
