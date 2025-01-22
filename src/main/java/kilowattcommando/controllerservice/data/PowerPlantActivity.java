package kilowattcommando.controllerservice.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name="PowerPlantActivity.findMissingPowerPlants", query="select p from PowerPlantActivity p where p.lastActivity < TIMESTAMPADD(MINUTE, -2, CURRENT_TIMESTAMP)")
public class PowerPlantActivity {
    @Id
    private String name;
    private Timestamp lastActivity;
}
