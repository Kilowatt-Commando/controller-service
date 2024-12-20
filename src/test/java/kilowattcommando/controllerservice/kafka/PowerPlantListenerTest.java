package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantLoggingHandler;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    private KafkaTemplate<String, PowerplantStatus> kafkaTemplate;

    @Autowired
    private PowerPlantStatusHandler powerPlantStatusHandler;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.serverAddress", KAFKA::getIpAddress);
        registry.add("kafka.serverPort", KAFKA::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    @Test
    void shouldHandlePowerplantStatusEvent() {
        PowerplantStatus powerplantStatus = new PowerplantStatus();
        powerplantStatus.name = "powerplant1";

        kafkaTemplate.send("powerplant", powerplantStatus);

        await()
                .pollInterval(3, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1, ((PowerPlantHandlerStub) powerPlantStatusHandler).getMessageCount());
                });
    }


    /**
     * Configuration of the KafkaProducer for testing purposes
     */
    @TestConfiguration
    public static class KafkaProducerConfig {
        @Value("${kafka.serverPort}")
        private String kafkaServerPort;

        @Value("${kafka.serverAddress}")
        private String kafkaServerAddress;

        @Bean
        public <T> ProducerFactory<String, T> producerFactory() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerAddress + ":" + kafkaServerPort);
            configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configs);
        }

        @Bean
        public <T> KafkaTemplate<String, T> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }

        @Bean
        public PowerPlantStatusHandler powerPlantStatusHandler() {
            return new PowerPlantHandlerStub();
        }
    }
}
