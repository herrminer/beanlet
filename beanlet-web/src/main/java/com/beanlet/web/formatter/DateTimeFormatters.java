package com.beanlet.web.formatter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFormatters {

  private static final DateTimeFormatter DATE_KEY_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd");

  public static DateTimeFormatter dateKeyFormatter() {
    return DATE_KEY_FORMATTER;
  }
}
