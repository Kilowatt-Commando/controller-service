package kilowattcommando.controllerservice.kafka;

import dto.DTOJsonDeserializer;
import dto.PowerplantCommand;
import dto.PowerplantOperation;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
        })
@DirtiesContext
@ExtendWith(KafkaContainerExtension.class)
public class CommandSenderTest {

    @Value("kafka.groupId")
    private String groupId;

    @Autowired
    private PowerPlantCommandSender powerPlantCommandSender;

    private KafkaConsumer<String, PowerplantOperation> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DTOJsonDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    @Test
    void startPowerPlant_1() throws Exception {
        powerPlantCommandSender.sendCommand("1", PowerplantCommand.start);
        try(KafkaConsumer<String, PowerplantOperation> consumer = createConsumer()) {
            consumer.subscribe(List.of("powerplant"));

            await()
                    .atMost(60, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                ConsumerRecords<String, PowerplantOperation> records = consumer.poll(Duration.ofSeconds(2));

                                assertEquals(1, records.count());
                                ConsumerRecord<String, PowerplantOperation> record = records.iterator().next();
                                assertEquals("1", new String(record.headers().lastHeader("targetConsumerId").value()));
                                assertEquals(PowerplantCommand.start, record.value().command);
                            }
                    );

        }
    }

}
