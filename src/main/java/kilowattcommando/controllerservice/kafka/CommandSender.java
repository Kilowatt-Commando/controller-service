package kilowattcommando.controllerservice.kafka;

import dto.PowerplantCommand;
import dto.PowerplantOperation;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandSender implements PowerPlantCommandSender {
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
    }
}
