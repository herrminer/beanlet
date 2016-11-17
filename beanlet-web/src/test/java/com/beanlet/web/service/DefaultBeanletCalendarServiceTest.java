package com.beanlet.web.service;

import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultBeanletCalendarServiceTest {

  private BeanletCalendarService.DefaultBeanletCalendarService service;

  @Before
  public void setUp() throws Exception {
    service = new BeanletCalendarService.DefaultBeanletCalendarService();
  }

  @Test
  public void getBeanletCalendar() throws Exception {
    int year = 2016;
    int month = 11;

    BeanletCalendar calendar = service.getBeanletCalendar(year, month);

    // current
    assertThat(calendar.getYear()).isEqualTo(year);
    assertThat(calendar.getMonth()).isEqualTo(month);

    // previous data
    assertThat(calendar.getPreviousYear()).isEqualTo(year);
    assertThat(calendar.getPreviousMonth()).isEqualTo(10);

    // next data
    assertThat(calendar.getNextYear()).isEqualTo(year);
    assertThat(calendar.getNextMonth()).isEqualTo(12);

    // correct number of days in the calendar
    List<BeanletCalendarDay> days = calendar.getDays();
    assertThat(days.size()).isEqualTo(BeanletCalendarService.DefaultBeanletCalendarService.DAYS_IN_CALENDAR);

    assertDay(days.get(0), 2016, 10, 30, false);
    assertDay(days.get(1), 2016, 10, 31, false);
    assertDay(days.get(2), 2016, 11, 1, true);
    assertDay(days.get(31), 2016, 11, 30, true);
    assertDay(days.get(32), 2016, 12, 1, false);
    assertDay(days.get(34), 2016, 12, 3, false);
  }

  private void assertDay(BeanletCalendarDay day,
                         int expectedYear,
                         int expectedMonth,
                         int expectedDayOfMonth,
                         boolean currentMonth) {
    assertThat(day.getYear()).isEqualTo(expectedYear);
    assertThat(day.getMonth()).isEqualTo(expectedMonth);
    assertThat(day.getDayOfMonth()).isEqualTo(expectedDayOfMonth);
    assertThat(day.isCurrentMonth()).isEqualTo(currentMonth);
  }

}