package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PowerPlantListener {
    @KafkaListener(topics = "${kafka.powerplant.topic}", groupId = "${kafka.groupId}")
    public void listen(ConsumerRecord<String, ?> message) {
            if(message.value() instanceof PowerplantStatus) {
                PowerplantStatus powerplantStatus = (PowerplantStatus) message.value();
                System.out.println(powerplantStatus);
            }
    }
}
