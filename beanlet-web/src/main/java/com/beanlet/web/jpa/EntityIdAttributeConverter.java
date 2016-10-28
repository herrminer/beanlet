package com.beanlet.web.jpa;

import javax.persistence.AttributeConverter;

public class EntityIdAttributeConverter implements AttributeConverter<EntityId, String> {

  @Override
  public String convertToDatabaseColumn(EntityId attribute) {
    return attribute.getValue();
  }

  @Override
  public EntityId convertToEntityAttribute(String dbData) {
    return new EntityId(dbData);
  }

}
