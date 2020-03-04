package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import environment.AddressesInterface;
import models.Recipe;

import java.io.IOException;
import java.util.List;

public class RecipeRefSerializer extends StdSerializer<List<Recipe>> {

    public RecipeRefSerializer() {
        this(null);
    }

    public RecipeRefSerializer(Class<List<Recipe>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Recipe> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (Recipe r : value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", r.getId());
            jgen.writeStringField("title", r.getTitle());
            jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/recipe/" + r.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

    }
}
