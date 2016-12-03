package com.beanlet.web.formatter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFormatters {

  private static final DateTimeFormatter DATE_KEY_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd");

  private static final DateTimeFormatter DATE_DISPLAY_FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy");

  private static final DateTimeFormatter DATE_TIME_SPACED = DateTimeFormat.forPattern("MM/dd/yyyy h mm a");

  public static DateTimeFormatter dateKeyFormatter() {
    return DATE_KEY_FORMATTER;
  }

  public static DateTimeFormatter dateDisplayFormatter() {
    return DATE_DISPLAY_FORMATTER;
  }

  public static DateTimeFormatter dateTimeSpaced() {
    return DATE_TIME_SPACED;
  }
}
