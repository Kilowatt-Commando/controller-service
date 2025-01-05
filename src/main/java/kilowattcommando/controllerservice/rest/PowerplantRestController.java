package kilowattcommando.controllerservice.rest;

import dto.PowerplantCommand;
import kilowattcommando.controllerservice.kafka.PowerPlantCommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/powerplant/{id}")
public class PowerplantRestController {
    private static final Logger log = LoggerFactory.getLogger(PowerplantRestController.class);

    private final PowerPlantCommandSender powerPlantCommandSender;

    public PowerplantRestController(PowerPlantCommandSender powerPlantCommandSender) {
        this.powerPlantCommandSender = powerPlantCommandSender;
    }

    @PutMapping("/start")
    private ResponseEntity<String> startPowerplant(@PathVariable String id) {
        log.info("Request to start powerplant with id {}", id);
        powerPlantCommandSender.sendCommand(id, PowerplantCommand.start);
        return ResponseEntity.ok("Powerplant started");
    }

    @PutMapping("/stop")
    private ResponseEntity<String> stopPowerplant(@PathVariable String id) {
        log.info("Request to stop powerplant with id {}", id);
        powerPlantCommandSender.sendCommand(id, PowerplantCommand.stop);
        return ResponseEntity.ok("Powerplant stopped");
    }

    @PutMapping("/gateClosure")
    private ResponseEntity<String> changeGateClosure(@PathVariable String id, @RequestBody GateClosure gateClosure) {
        log.info("Request to change gate closure to {} which is {} on powerplant with id {}", gateClosure.closure(), gateClosure.validate() ? "valid" : "not valid" , id);
        if(gateClosure.validate()) {
            powerPlantCommandSender.sendCommand(id, gateClosure.getEquivalentPowerplantCommand());
            return ResponseEntity.ok("Changed gate closure");
        } else {
            return ResponseEntity.badRequest().body("Invalid gate closure");
        }

    }
}
