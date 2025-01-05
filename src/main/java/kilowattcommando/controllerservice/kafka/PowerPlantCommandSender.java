package kilowattcommando.controllerservice.kafka;

public interface PowerPlantCommandSender {
    void sendCommand(String powerPlantId, String command);
}
