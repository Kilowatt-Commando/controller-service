package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PowerPlantListener {

    private final PowerPlantStatusHandler powerPlantStatusHandler;

    public PowerPlantListener(PowerPlantStatusHandler powerPlantStatusHandler) {
        this.powerPlantStatusHandler = powerPlantStatusHandler;
    }

    @KafkaListener(topics = "${kafka.powerplant.topic}", groupId = "${kafka.groupId}")
    public void listen(ConsumerRecord<String, ?> message) {
            if(message.value() instanceof PowerplantStatus powerplantStatus) {
                powerPlantStatusHandler.handle(powerplantStatus);
            }
    }
}
