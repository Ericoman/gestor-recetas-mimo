package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import environment.AddressesInterface;
import models.Nutrition;

import java.io.IOException;

public class SingleNutritionRefSerializer extends StdSerializer<Nutrition> {

    public SingleNutritionRefSerializer() {
        this(null);
    }

    public SingleNutritionRefSerializer(Class<Nutrition> t) {
        super(t);
    }

    @Override
    public void serialize(
            Nutrition value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/nutrition/" + value.getId());
        jgen.writeEndObject();

    }
}
