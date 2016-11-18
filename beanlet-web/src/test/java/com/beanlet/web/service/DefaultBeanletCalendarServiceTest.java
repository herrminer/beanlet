package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DefaultBeanletCalendarServiceTest {

  private BeanletCalendarService.DefaultBeanletCalendarService service;

  @MockBean
  private BeanRepository beanRepository;

  @Before
  public void setUp() throws Exception {
    service = new BeanletCalendarService.DefaultBeanletCalendarService();
    service.setBeanRepository(beanRepository);
  }

  @Test
  public void getBeanletCalendar() throws Exception {
    EntityId<Beanlet> beanletId = new EntityId<>("12345");
    int year = 2016;
    int month = 11;

//    service.setToday(new DateTime(2016, 11, 18, 0, 0, 0, 0, DateTimeZone.UTC));
    service.setToday(new DateTime(2016, 11, 18, 0, 0, 0, 0, DateTimeZone.UTC));
    BeanletCalendar calendar = service.getBeanletCalendar(beanletId, year, month, DateTimeZone.UTC);

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
    assertDay(days.get(19), 2016, 11, 18, true, true);
    assertDay(days.get(31), 2016, 11, 30, true);
    assertDay(days.get(32), 2016, 12, 1, false);
    assertDay(days.get(34), 2016, 12, 3, false);
  }

  private void assertDay(BeanletCalendarDay day,
                         int expectedYear,
                         int expectedMonth,
                         int expectedDayOfMonth,
                         boolean currentMonth) {
    assertDay(day, expectedYear, expectedMonth, expectedDayOfMonth, currentMonth, false);
  }

  private void assertDay(BeanletCalendarDay day,
                         int expectedYear,
                         int expectedMonth,
                         int expectedDayOfMonth,
                         boolean currentMonth,
                         boolean today) {
    assertThat(day.getYear()).isEqualTo(expectedYear);
    assertThat(day.getMonth()).isEqualTo(expectedMonth);
    assertThat(day.getDayOfMonth()).isEqualTo(expectedDayOfMonth);
    assertThat(day.isCurrentMonth()).isEqualTo(currentMonth);
    assertThat(day.isToday()).isEqualTo(today);
  }

}