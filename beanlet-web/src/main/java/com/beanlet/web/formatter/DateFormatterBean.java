package com.beanlet.web.formatter;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import static com.beanlet.web.formatter.DateTimeFormatters.*;

@Component("fmt")
public class DateFormatterBean {

  public String display(DateTime dateTime) {
    return dateDisplayFormatter().print(dateTime);
  }

  public String spaced(DateTime dateTime) {
    return dateTimeSpaced().print(dateTime);
  }

}
