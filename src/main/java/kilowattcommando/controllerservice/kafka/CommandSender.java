package kilowattcommando.controllerservice.kafka;

import dto.PowerplantCommand;
import dto.PowerplantOperation;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandSender implements PowerPlantCommandSender {
    private static final Logger log = LoggerFactory.getLogger(PowerPlantCommandSender.class);

    @Value("${kafka.controlTopic}")
    private String powerPlantControlTopic;

    @Autowired
    private KafkaTemplate<String, PowerplantOperation> kafkaTemplate;

    @Override
    public void sendCommand(String powerPlantId, PowerplantCommand command) {
        PowerplantOperation powerplantOperation = new PowerplantOperation(command);
        ProducerRecord<String, PowerplantOperation> commandRecord = new ProducerRecord<>(powerPlantControlTopic, powerplantOperation);
        commandRecord.headers().add("targetConsumerId", powerPlantId.getBytes());
        kafkaTemplate.send(commandRecord);
        log.info("Sent command {} to powerplant {}", command, powerPlantId);
    }
}
