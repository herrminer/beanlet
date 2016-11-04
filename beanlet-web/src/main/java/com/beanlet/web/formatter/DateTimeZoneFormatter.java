package com.beanlet.web.formatter;

import com.beanlet.web.jpa.EntityId;
import org.joda.time.DateTimeZone;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class DateTimeZoneFormatter implements Formatter<DateTimeZone> {
  @Override
  public DateTimeZone parse(String text, Locale locale) throws ParseException {
    return DateTimeZone.forID(text);
  }

  @Override
  public String print(DateTimeZone object, Locale locale) {
    return object.getID();
  }
}
