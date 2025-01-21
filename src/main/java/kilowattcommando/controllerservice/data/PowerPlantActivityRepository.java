package kilowattcommando.controllerservice.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerPlantActivityRepository extends CrudRepository<PowerPlantActivity, String> {

}
