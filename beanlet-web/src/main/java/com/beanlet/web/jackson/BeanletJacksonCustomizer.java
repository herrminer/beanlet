package com.beanlet.web.jackson;

import com.beanlet.web.jpa.EntityId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class BeanletJacksonCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

  @Autowired
  private EntityIdSerializer entityIdSerializer;

  @Autowired
  private EntityIdDeserializer entityIdDeserializer;

  @Override
  public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
    jacksonObjectMapperBuilder.serializerByType(EntityId.class, entityIdSerializer);
    jacksonObjectMapperBuilder.deserializerByType(EntityId.class, entityIdDeserializer);
  }
}
