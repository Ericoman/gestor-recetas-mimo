package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.Ingredient;

import java.io.IOException;
import java.util.List;

import environment.AddressesInterface;
public class IngredientRefSerializer extends StdSerializer<List<Ingredient>> {

    public IngredientRefSerializer() {
        this(null);
    }

    public IngredientRefSerializer(Class<List<Ingredient>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Ingredient> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (Ingredient i : value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", i.getId());
            jgen.writeStringField("name", i.getName());
            jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/ingredient/" + i.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

    }
}
