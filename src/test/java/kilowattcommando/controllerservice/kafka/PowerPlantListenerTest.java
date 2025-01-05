package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
})
@Testcontainers
public class PowerPlantListenerTest {

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
    );
    @Autowired
    private KafkaTemplate<String, PowerplantStatus> kafkaTestTemplate;

    @Autowired
    private PowerPlantStatusHandler powerPlantStatusHandler;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.serverAddress", KAFKA::getIpAddress);
        registry.add("kafka.serverPort", KAFKA::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("kafka.controlTopic", () -> "powerplant");
    }

    @Test
    void shouldHandlePowerplantStatusEvent() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "powerplant1";

        kafkaTestTemplate.send("backend", powerplantStatus);

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
