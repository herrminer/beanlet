package com.beanlet.web;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;

public class TestUtils {

  public static final EntityId<User> HERRMINER = new EntityId<>("52f06c519e0f42dead1ba516bb82cca7");
  public static final EntityId<User> FRAUMINER = new EntityId<>("d00ae4150090458eb1ebab71d9b7e522");
  public static final EntityId<Beanlet> SCRIPTURE_READING = new EntityId<>("6b304b64e03e478c890aee5efb7186d3");
  public static final EntityId<Beanlet> EXERCISE = new EntityId<>("fe6d642c80424153b842229acc7bd2f9");
  public static final EntityId<Bean> BEAN_ID = new EntityId<>("ae3018d456114794a5d35ba7d5a4d180");

  public static String uuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static DateTime testUtcDate() {
    return new DateTime(2016, 6, 1, 12, 0, 0, DateTimeZone.UTC);
  }

  public static DateTime testDateCentral() {
    return new DateTime(2016, 6, 1, 12, 0, 0, DateTimeZone.forID("America/Chicago"));
  }

  public static DateTime testDate(DateTimeZone timeZone) {
    return new DateTime(2016, 6, 1, 12, 0, 0, timeZone);
  }
}
