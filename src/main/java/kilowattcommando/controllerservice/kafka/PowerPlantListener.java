package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantActivityHandler;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PowerPlantListener {

    private final PowerPlantStatusHandler powerPlantStatusHandler;
    private final PowerPlantActivityHandler powerPlantActivityHandler;

    public PowerPlantListener(PowerPlantStatusHandler powerPlantStatusHandler, PowerPlantActivityHandler powerPlantActivityHandler) {
        this.powerPlantStatusHandler = powerPlantStatusHandler;
        this.powerPlantActivityHandler = powerPlantActivityHandler;
    }

    @KafkaListener(topics = "${kafka.powerplant.topic}", groupId = "${kafka.groupId}")
    public void listen(ConsumerRecord<String, ?> message) {
            if(message.value() instanceof PowerplantStatus powerplantStatus) {
                powerPlantStatusHandler.handle(powerplantStatus);
                powerPlantActivityHandler.handle(powerplantStatus);
            }
    }
}
