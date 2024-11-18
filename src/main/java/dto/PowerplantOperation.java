package dto;

public class PowerplantOperation {
    public PowerplantCommand command;

    public PowerplantOperation(PowerplantCommand command) {
        this.command = command;
    }

    //DO NOT DELETE (NEEDED FOR JSON DESERIALIZATION)
    public PowerplantOperation() {
    }
}
