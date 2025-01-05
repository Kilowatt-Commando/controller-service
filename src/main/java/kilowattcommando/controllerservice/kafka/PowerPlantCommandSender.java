package kilowattcommando.controllerservice.kafka;

public interface PowerPlantCommandSender {
    void sendCommand(Long powerPlantId, String command);
}
