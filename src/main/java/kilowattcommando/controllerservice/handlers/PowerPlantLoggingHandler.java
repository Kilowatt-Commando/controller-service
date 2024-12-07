package kilowattcommando.controllerservice.handlers;

import dto.PowerplantStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PowerPlantLoggingHandler implements PowerPlantStatusHandler
{
    private static final Logger log = LoggerFactory.getLogger(PowerPlantLoggingHandler.class);

    @Override
    public void handle(PowerplantStatus status) {
        log.info(String.format("New status: %s", status.toString()));
    }
}
