package kilowattcommando.controllerservice.kafka;

import dto.PowerplantStatus;
import kilowattcommando.controllerservice.handlers.PowerPlantStatusHandler;
import lombok.Getter;

@Getter
public class PowerPlantHandlerStub implements PowerPlantStatusHandler {
    private int messageCount = 0;

    @Override
    public void handle(PowerplantStatus status) {
        messageCount++;
    }

}
