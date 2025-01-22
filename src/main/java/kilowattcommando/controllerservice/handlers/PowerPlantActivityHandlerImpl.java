package kilowattcommando.controllerservice.handlers;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.data.PowerPlantActivity;
import kilowattcommando.controllerservice.data.PowerPlantActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class PowerPlantActivityHandlerImpl implements PowerPlantActivityHandler{
    private final PowerPlantActivityRepository powerPlantActivityRepository;

    private static final Logger log = LoggerFactory.getLogger(PowerPlantActivityHandlerImpl.class);

    public PowerPlantActivityHandlerImpl(PowerPlantActivityRepository powerPlantActivityRepository) {
        this.powerPlantActivityRepository = powerPlantActivityRepository;
    }

    @Override
    public void handle(PowerplantStatus status) {
        PowerPlantActivity powerPlantActivity = new PowerPlantActivity(status.name, Timestamp.from(Instant.now()));
        log.info("Received PowerPlantActivity: {}", powerPlantActivity.getName());
        powerPlantActivityRepository.save(powerPlantActivity);
    }
}
