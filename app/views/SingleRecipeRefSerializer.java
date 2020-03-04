package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import environment.AddressesInterface;
import models.Recipe;

import java.io.IOException;

public class SingleRecipeRefSerializer extends StdSerializer<Recipe> {

    public SingleRecipeRefSerializer() {
        this(null);
    }

    public SingleRecipeRefSerializer(Class<Recipe> t) {
        super(t);
    }

    @Override
    public void serialize(
            Recipe value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("title", value.getTitle());
        jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/recipe/" + value.getId());
        jgen.writeEndObject();

    }
}
