package kilowattcommando.controllerservice.rest;

import jakarta.websocket.server.PathParam;
import kilowattcommando.controllerservice.handlers.PowerPlantLoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/powerplant/{id}")
public class PowerplantRestController {
    private static final Logger log = LoggerFactory.getLogger(PowerPlantLoggingHandler.class);

    @PutMapping("/start")
    private ResponseEntity<String> startPowerplant(@PathVariable Long id) {
        log.info("Request to start powerplant with id {}", id);
        return ResponseEntity.ok("Powerplant started");
    }

    @PutMapping("/stop")
    private ResponseEntity<String> stopPowerplant(@PathVariable Long id) {
        log.info("Request to stop powerplant with id {}", id);
        return ResponseEntity.ok("Powerplant stopped");
    }

    @PutMapping("/gateClosure")
    private ResponseEntity<String> changeGateClosure(@PathVariable Long id, @RequestBody GateClosure gateClosure) {
        log.info("Request to change gate closure to {} which is {} on powerplant with id {}", gateClosure.closure(), gateClosure.validate() ? "valid" : "not valid" , id);
        if(gateClosure.validate()) {
            return ResponseEntity.ok("Changed gate closure");
        } else {
            return ResponseEntity.badRequest().body("Invalid gate closure");
        }

    }
}
