package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import environment.AddressesInterface;
import models.Category;
import models.Ingredient;

import java.io.IOException;
import java.util.List;

public class CategoryRefSerializer extends StdSerializer<List<Category>> {

    public CategoryRefSerializer() {
        this(null);
    }

    public CategoryRefSerializer(Class<List<Category>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Category> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (Category c : value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", c.getId());
            jgen.writeStringField("name", c.getName());
            jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/category/" + c.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

    }
}
