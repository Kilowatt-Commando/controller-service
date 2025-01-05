package kilowattcommando.controllerservice.rest;

public record GateClosure(String closure) {
    public boolean validate() {
        return closure.matches("OPEN|CLOSED|HALF|QUARTER|THREE_QUARTERS");
    }
}
