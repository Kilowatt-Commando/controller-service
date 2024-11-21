package dto;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class DTOJsonDeserializer<T> extends JsonDeserializer<T> {

    public DTOJsonDeserializer() {
        super();

        this.addTrustedPackages("dto");

    }
}