package kilowattcommando.controllerservice;

import dto.DTOJsonDeserializer;
import dto.PowerplantCommand;
import dto.PowerplantOperation;
import kilowattcommando.controllerservice.kafka.KafkaContainerExtension;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.application.name=controller-service"
        })
@ExtendWith(KafkaContainerExtension.class)
@DirtiesContext
@AutoConfigureMockMvc
public class CommandIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Value("kafka.groupId")
    private String groupId;

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
        mockMvc.perform(put("/powerplant/1/start")).andExpect(status().isOk());
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

    @Test
    void startPowerPlant_1_and_setGateToHalf() throws Exception {
        mockMvc.perform(put("/powerplant/1/start")).andExpect(status().isOk());
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\": \"HALF\"}")).andExpect(status().isOk());
        try(KafkaConsumer<String, PowerplantOperation> consumer = createConsumer()) {
            consumer.subscribe(List.of("powerplant"));

            await()
                    .atMost(60, TimeUnit.SECONDS)
                    .untilAsserted(
                            () -> {
                                ConsumerRecords<String, PowerplantOperation> records = consumer.poll(Duration.ofSeconds(2));

                                assertEquals(2, records.count());

                                Iterator<ConsumerRecord<String, PowerplantOperation>> iterator = records.iterator();
                                ConsumerRecord<String, PowerplantOperation> record = iterator.next();
                                ConsumerRecord<String, PowerplantOperation> record2 = iterator.next();

                                assertEquals("1", new String(record.headers().lastHeader("targetConsumerId").value()));
                                assertEquals(PowerplantCommand.start, record.value().command);

                                assertEquals("1", new String(record2.headers().lastHeader("targetConsumerId").value()));
                                assertEquals(PowerplantCommand.gateHalf, record2.value().command);
                            }
                    );

        }
    }
}
