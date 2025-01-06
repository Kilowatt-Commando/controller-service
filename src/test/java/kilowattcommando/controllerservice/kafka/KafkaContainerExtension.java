package kilowattcommando.controllerservice.kafka;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainerExtension implements BeforeAllCallback, AfterAllCallback {
    protected static KafkaContainer KAFKA;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

        KAFKA.start();

        System.setProperty("kafka.serverAddress", KAFKA.getIpAddress());
        System.setProperty("kafka.serverPort", String.valueOf(KAFKA.getFirstMappedPort()));
        System.setProperty("spring.kafka.bootstrap-servers", KAFKA.getBootstrapServers());
        System.setProperty("kafka.powerplant.topic", "backend");
        System.setProperty("kafka.controlTopic", "powerplant");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        // This is done by testcontainers
    }
}
