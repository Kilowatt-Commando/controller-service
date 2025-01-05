package kilowattcommando.controllerservice.kafka;

import dto.PowerplantCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommandSender implements PowerPlantCommandSender {
    @Autowired
    private KafkaTemplate<String, PowerplantCommand> kafkaTemplate;

    @Override
    public void sendCommand(String powerPlantId, String command) {
    }
}
