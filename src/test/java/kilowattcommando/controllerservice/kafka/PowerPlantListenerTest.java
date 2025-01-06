package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    void shouldHandlePowerplantStatusEvent() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "powerplant1";

        KafkaProducer<String, PowerplantStatus> producer = createProducer();

        ProducerRecord<String, PowerplantStatus> record = new ProducerRecord<>("backend", powerplantStatus);

        log.info("Sent record: {}", record);
        producer.send(record);

        log.info("Sent record: {}", record);

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
        public <T> ProducerFactory<String, T> producerTestFactory() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerAddress + ":" + kafkaServerPort);
            configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configs);
        }

        @Bean
        public <T> KafkaTemplate<String, T> kafkaTestTemplate() {
            return new KafkaTemplate<>(producerTestFactory());
        }

        @Bean
        public PowerPlantStatusHandler powerPlantStatusHandler() {
            return new PowerPlantHandlerStub();
        }
    }
}
