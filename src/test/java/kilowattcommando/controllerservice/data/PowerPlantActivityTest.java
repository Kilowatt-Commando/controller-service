package kilowattcommando.controllerservice.data;

import dto.PowerplantOperation;
import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantActivityHandler;
import kilowattcommando.controllerservice.handlers.PowerPlantActivityHandlerImpl;
import kilowattcommando.controllerservice.kafka.CommandSender;
import kilowattcommando.controllerservice.kafka.KafkaConsumerConfig;
import kilowattcommando.controllerservice.kafka.KafkaContainerExtension;
import kilowattcommando.controllerservice.kafka.KafkaProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
        })
@DirtiesContext
@ExtendWith(KafkaContainerExtension.class)
public class PowerPlantActivityTest {
    @Autowired
    private PowerPlantActivityHandler powerPlantActivityHandlerImpl;

    @Autowired
    private PowerPlantActivityRepository powerPlantActivityRepository;

    @BeforeEach
    void resetRepository() {
        powerPlantActivityRepository.deleteAll();
    }

    @Test
    void handlePowerPlantActivity() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "test";

        powerPlantActivityHandlerImpl.handle(powerplantStatus);

        assertEquals(1, powerPlantActivityRepository.count());
    }

    @Test
    void handlePowerPlantActivityUpdateToNewTimestamp() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "test";

        powerPlantActivityHandlerImpl.handle(powerplantStatus);

        Timestamp firstActivity = powerPlantActivityRepository.findById("test").get().getLastActivity();

        powerPlantActivityHandlerImpl.handle(powerplantStatus);

        Timestamp lastActivity = powerPlantActivityRepository.findById("test").get().getLastActivity();

        assertTrue(lastActivity.after(firstActivity));
    }

    @Test
    void handlePowerPlantActivityMultiplePlants() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "test1";

        powerPlantActivityHandlerImpl.handle(powerplantStatus);
        Timestamp firstActivity = powerPlantActivityRepository.findById("test1").get().getLastActivity();

        powerplantStatus.name = "test2";

        powerPlantActivityHandlerImpl.handle(powerplantStatus);
        Timestamp secondActivity = powerPlantActivityRepository.findById("test2").get().getLastActivity();

        assertEquals(2, powerPlantActivityRepository.count());
        assertTrue(secondActivity.after(firstActivity));
    }

    @Test
    void handlePowerPlantActivityUpdateCountUnchanged() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "test";

        powerPlantActivityHandlerImpl.handle(powerplantStatus);
        powerPlantActivityHandlerImpl.handle(powerplantStatus);

        assertEquals(1, powerPlantActivityRepository.count());
    }
}
