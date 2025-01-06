package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
})
@DirtiesContext
@ExtendWith(KafkaContainerExtension.class)
public class PowerPlantListenerTest {

    private static final Logger log = LoggerFactory.getLogger(PowerPlantListenerTest.class);
    @Autowired
    private PowerPlantStatusHandler powerPlantStatusHandler;

    private KafkaProducer<String, PowerplantStatus> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    @Test
    void shouldHandlePowerplantStatusEvent() throws InterruptedException {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "powerplant1";

        KafkaProducer<String, PowerplantStatus> producer = createProducer();


        ProducerRecord<String, PowerplantStatus> record = new ProducerRecord<>("backend", powerplantStatus);

        Thread.sleep(500);
        producer.send(record);
        Thread.sleep(500);

        await()
                .pollInterval(3, TimeUnit.SECONDS)
                .atMost(60, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Assertions.assertEquals(1, ((PowerPlantHandlerStub) powerPlantStatusHandler).getMessageCount());
                });
    }


    /**
     * Configuration of the KafkaProducer for testing purposes
     */
    @TestConfiguration
    public static class KafkaProducerTestConfig {
        @Value("${kafka.serverPort}")
        private String kafkaServerPort;

        @Value("${kafka.serverAddress}")
        private String kafkaServerAddress;

        @Bean
        public PowerPlantStatusHandler powerPlantStatusHandler() {
            return new PowerPlantHandlerStub();
        }
    }
}
