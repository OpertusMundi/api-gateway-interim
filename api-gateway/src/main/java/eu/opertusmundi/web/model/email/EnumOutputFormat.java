package eu.opertusmundi.web.model.email;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum EnumOutputFormat {

    /*
     * Unsupported format
     */
    UNDEFINED(1),
    /*
     * Plain text format
     */
    TEXT(1),
    /*
     * HTML5 format
     */
    HTML(2),
    ;

    private final int value;

    private EnumOutputFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static EnumOutputFormat fromString(String value) {
        for (final EnumOutputFormat item : EnumOutputFormat.values()) {
            if (item.name().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return EnumOutputFormat.UNDEFINED;
    }

    public static class Deserializer extends JsonDeserializer<EnumOutputFormat> {

        @Override
        public EnumOutputFormat deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            return EnumOutputFormat.fromString(parser.getValueAsString());
        }
    }
}
