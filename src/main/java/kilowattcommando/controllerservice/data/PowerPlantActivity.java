package kilowattcommando.controllerservice.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerPlantActivity {
    @Id
    private String name;
    private Timestamp lastActivity;
}
