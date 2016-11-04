package com.beanlet.web.formatter;

import com.beanlet.web.jpa.EntityId;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class EntityIdFormatter implements Formatter<EntityId> {
  @Override
  public EntityId parse(String text, Locale locale) throws ParseException {
    return new EntityId(text);
  }

  @Override
  public String print(EntityId object, Locale locale) {
    return object.getValue();
  }
}
