package dto;

public class PowerplantStatus {
    public String name;
    public PlantStatus status;
    public int rpm;
    public float outputVoltage, frequency, waterThroughput;
    public PlantWeather nextWeather;

    public PowerplantStatus(String name, PlantStatus status, int rpm, float outputVoltage, float frequency, float waterThroughput, PlantWeather nextWeather) {
        this.name = name;
        this.status = status;
        this.rpm = rpm;
        this.outputVoltage = outputVoltage;
        this.frequency = frequency;
        this.waterThroughput = waterThroughput;
        this.nextWeather = nextWeather;
    }

    //DO NOT DELETE (NEEDED FOR JSON DESERIALIZATION)
    public PowerplantStatus() {
    }

    @Override
    public String toString() {
        return "PowerplantStatus{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", rpm=" + rpm +
                ", outputVoltage=" + outputVoltage +
                ", frequency=" + frequency +
                ", waterThroughput=" + waterThroughput +
                ", nextWeather=" + nextWeather +
                '}';
    }
}
