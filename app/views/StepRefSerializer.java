package views;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import environment.AddressesInterface;
import models.Ingredient;
import models.Step;

import java.io.IOException;
import java.util.List;

public class StepRefSerializer extends StdSerializer<List<Step>> {

    public StepRefSerializer() {
        this(null);
    }

    public StepRefSerializer(Class<List<Step>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Step> value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (Step s : value) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", s.getId());
            jgen.writeNumberField("number", s.getNumber());
            jgen.writeStringField("title", s.getTitle());
            jgen.writeStringField("link", AddressesInterface.http_completeAddress +"/step/" + s.getId());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();

    }
}
