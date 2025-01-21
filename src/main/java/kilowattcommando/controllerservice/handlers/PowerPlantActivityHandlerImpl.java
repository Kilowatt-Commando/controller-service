package kilowattcommando.controllerservice.handlers;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.data.PowerPlantActivity;
import kilowattcommando.controllerservice.data.PowerPlantActivityRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class PowerPlantActivityHandlerImpl implements PowerPlantActivityHandler{
    private final PowerPlantActivityRepository powerPlantActivityRepository;

    public PowerPlantActivityHandlerImpl(PowerPlantActivityRepository powerPlantActivityRepository) {
        this.powerPlantActivityRepository = powerPlantActivityRepository;
    }

    @Override
    public void handle(PowerplantStatus status) {
        PowerPlantActivity powerPlantActivity = new PowerPlantActivity(status.name, Timestamp.from(Instant.now()));
        powerPlantActivityRepository.save(powerPlantActivity);
    }
}
