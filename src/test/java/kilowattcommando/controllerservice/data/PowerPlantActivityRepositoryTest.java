package kilowattcommando.controllerservice.data;

import kilowattcommando.controllerservice.kafka.KafkaContainerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
        })
@DirtiesContext
@ExtendWith(KafkaContainerExtension.class)
public class PowerPlantActivityRepositoryTest {
    @Autowired
    private PowerPlantActivityRepository powerPlantActivityRepository;

    @BeforeEach
    void resetRepository() {
        powerPlantActivityRepository.deleteAll();
    }

    @Test
    void noMissingPowerPlants() {
        PowerPlantActivity powerPlantActivity = new PowerPlantActivity("test", Timestamp.from(Instant.now()));

        powerPlantActivityRepository.save(powerPlantActivity);

        assertFalse(powerPlantActivityRepository.findMissingPowerPlants().iterator().hasNext());
    }

    @Test
    void singleMissingPowerPlant() {
        PowerPlantActivity powerPlantActivity = new PowerPlantActivity("test", Timestamp.from(Instant.ofEpochSecond(10000)));

        powerPlantActivityRepository.save(powerPlantActivity);

        assertTrue(powerPlantActivityRepository.findMissingPowerPlants().iterator().hasNext());
    }

    @Test
    void multipleMissingPowerPlant() {
        PowerPlantActivity powerPlantActivity = new PowerPlantActivity("test", Timestamp.from(Instant.ofEpochSecond(10000)));
        PowerPlantActivity powerPlantActivity2 = new PowerPlantActivity("test2", Timestamp.from(Instant.ofEpochSecond(100000)));

        powerPlantActivityRepository.save(powerPlantActivity);
        powerPlantActivityRepository.save(powerPlantActivity2);

        Stream<PowerPlantActivity> stream = powerPlantActivityRepository.findMissingPowerPlants().stream();

        assertTrue(stream.allMatch(x -> x.equals(powerPlantActivity) || x.equals(powerPlantActivity2)));
    }
}
