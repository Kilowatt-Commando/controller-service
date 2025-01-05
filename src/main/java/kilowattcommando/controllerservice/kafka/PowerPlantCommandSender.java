package kilowattcommando.controllerservice.kafka;

import dto.PowerplantCommand;
import org.springframework.stereotype.Service;

@Service
public interface PowerPlantCommandSender {
    void sendCommand(String powerPlantId, PowerplantCommand command);
}
