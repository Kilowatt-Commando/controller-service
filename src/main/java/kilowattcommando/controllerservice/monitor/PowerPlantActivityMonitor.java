package kilowattcommando.controllerservice.monitor;

import kilowattcommando.controllerservice.data.PowerPlantActivity;
import kilowattcommando.controllerservice.data.PowerPlantActivityRepository;
import kilowattcommando.controllerservice.handlers.PowerPlantLoggingHandler;
import kilowattcommando.controllerservice.push.PushNotificationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PowerPlantActivityMonitor {
    private PowerPlantActivityRepository powerPlantActivityRepository;
    private PushNotificationController pushNotificationController;

    private static final Logger log = LoggerFactory.getLogger(PowerPlantActivityMonitor.class);

    public PowerPlantActivityMonitor(PowerPlantActivityRepository powerPlantActivityRepository, PushNotificationController pushNotificationController) {
        this.powerPlantActivityRepository = powerPlantActivityRepository;
        this.pushNotificationController = pushNotificationController;
    }

    @Scheduled(fixedDelay = 120_000L)
    public void checkForMissingPowerPlants() {
        Streamable<PowerPlantActivity> missingPowerplants = this.powerPlantActivityRepository.findMissingPowerPlants();
        missingPowerplants.forEach(powerPlantActivity -> {
            log.error("Missing powerplant detected: {}", powerPlantActivity.getName());
            pushNotificationController.sendMessage(String.format("Connection with powerplant %s is lost.", powerPlantActivity.getName()));
        });
        if(missingPowerplants.isEmpty()) {
            log.info("No missing powerplant detected");
            pushNotificationController.sendMessage("No missing powerplants");
        }
    }
}
