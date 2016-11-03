package com.beanlet.web.jackson;

import com.beanlet.web.jpa.EntityId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntityIdDeserializer extends JsonDeserializer<EntityId> {
  @Override
  public EntityId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return new EntityId(p.getValueAsString());
  }
}
