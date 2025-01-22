package kilowattcommando.controllerservice.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerPlantActivityRepository extends CrudRepository<PowerPlantActivity, String> {
    Streamable<PowerPlantActivity> findMissingPowerPlants();
}
