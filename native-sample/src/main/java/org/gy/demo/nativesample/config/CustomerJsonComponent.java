package org.gy.demo.nativesample.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonComponent
public class CustomerJsonComponent {

    public static class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {
        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                long timestamp = value.toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }

    /**
     * 反序列化实现
     */
    public static class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            } else {
                return null;
            }
        }
    }
}
