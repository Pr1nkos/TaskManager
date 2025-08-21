package ru.pr1nkos.taskmanager.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.io.IOException;

public class HtmlSanitizingDeserializer extends JsonDeserializer<String> {

    private static final PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getValueAsString();
        if (text == null) return null;
        return sanitizer.sanitize(text);
    }
}
