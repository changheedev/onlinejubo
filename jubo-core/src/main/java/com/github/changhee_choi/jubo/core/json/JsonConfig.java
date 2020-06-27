package com.github.changhee_choi.jubo.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(jsonMapperJava8DateTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public Module jsonMapperJava8DateTimeModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            public void serialize(
                    LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate));
            }
        });

        module.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            public void serialize(
                    LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime));
            }
        });

        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            public void serialize(
                    LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime));
            }
        });

        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalDate.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        module.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        });

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        return module;
    }
}
