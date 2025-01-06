package kilowattcommando.controllerservice.rest;

import dto.PowerplantCommand;

public record GateClosure(String closure) {
    public boolean validate() {
        return closure.matches("OPEN|CLOSED|HALF|QUARTER|THREE_QUARTERS");
    }

    public PowerplantCommand getEquivalentPowerplantCommand() {
        switch (closure) {
            case "OPEN":
                return PowerplantCommand.gateOpen;
            case "CLOSED":
                return PowerplantCommand.gateClose;
            case "HALF":
                return PowerplantCommand.gateHalf;
            case "QUARTER":
                return PowerplantCommand.gateQuarter;
            case "THREE_QUARTERS":
                return PowerplantCommand.gateThreeQuarters;
            default:
                return null;
        }
    }
}
