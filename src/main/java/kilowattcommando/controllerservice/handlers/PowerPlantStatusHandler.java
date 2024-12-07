package kilowattcommando.controllerservice.handlers;

import dto.PowerplantStatus;
import org.springframework.stereotype.Service;

@Service
public interface PowerPlantStatusHandler {
    void handle(PowerplantStatus status);
}
