package com.beanlet.web.jackson;

import com.beanlet.web.jpa.EntityId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntityIdSerializer extends JsonSerializer<EntityId> {
  @Override
  public void serialize(EntityId value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    gen.writeString(value.getValue());
  }
}
