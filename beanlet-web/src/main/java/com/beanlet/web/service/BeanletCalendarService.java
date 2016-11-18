package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanRepository;
import com.beanlet.web.repository.BeanletRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BeanletCalendarService {

  BeanletCalendar getBeanletCalendar(EntityId<Beanlet> beanletId, int year, int month, DateTimeZone dateTimeZone);

  @Service
  class DefaultBeanletCalendarService implements BeanletCalendarService {

    public static final Logger logger = LoggerFactory.getLogger(DefaultBeanletCalendarService.class);

    private BeanRepository beanRepository;

    public static final int DAYS_IN_CALENDAR = 35;

    private static final  DateTimeFormatter KEY_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd");

    private DateTime today;

    @Override
    public BeanletCalendar getBeanletCalendar(EntityId<Beanlet> beanletId, int year, int month, DateTimeZone dateTimeZone) {
      if (today == null) { // may have been set via testing method
        today = new DateTime(dateTimeZone).withTime(0, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC);
      }

      DateTime currentMonth = new DateTime(year, month, 1, 0, 0, 0);
      DateTime previousMonth = currentMonth.minusMonths(1);
      DateTime nextMonth = currentMonth.plusMonths(1);

      List<BeanletCalendarDay> days = new ArrayList<>(DAYS_IN_CALENDAR);
      DateTime day = getFirstDayOfCalendar(currentMonth);
      Map<String, List<Bean>> beans = getBeansMap(beanletId, day, DAYS_IN_CALENDAR);

      for (int i=0; i < DAYS_IN_CALENDAR; i++){
        List<Bean> beansForDay = beans.get(KEY_FORMATTER.print(day));
        days.add(new BeanletCalendarDay(
          day.getYear(),
          day.getMonthOfYear(),
          day.getDayOfMonth(),
          day.getMonthOfYear() == month,
          day.isEqual(today),
          beansForDay == null ? 0 : beansForDay.size()));
        day = day.plusDays(1);
      }

      return new BeanletCalendar.Builder()
        .withYear(year)
        .withMonth(month)
        .withPreviousYear(previousMonth.getYear())
        .withPreviousMonth(previousMonth.getMonthOfYear())
        .withNextYear(nextMonth.getYear())
        .withNextMonth(nextMonth.getMonthOfYear())
        .withDays(days)
        .getCalendar();
    }

    Map<String, List<Bean>> getBeansMap(EntityId<Beanlet> beanletId, DateTime startDateTime, int daysToInclude) {
      DateTime endDateTime = startDateTime.plusDays(daysToInclude).withTime(23, 59, 59, 999);

      logger.debug("fetching beans for beanlet " + beanletId + " between " + startDateTime + " and " + endDateTime);

      List<Bean> beans = beanRepository.findByBeanletIdAndLocalDateBetween(beanletId, startDateTime, endDateTime);
      Map<String, List<Bean>> result = new HashMap<>();
      String key;

      for (Bean bean : beans) {
        key = KEY_FORMATTER.print(bean.getLocalDate());
        if (!result.containsKey(key)) {
          result.put(key, new ArrayList<>());
        }
        result.get(key).add(bean);
      }

      return result;
    }

    private DateTime getFirstDayOfCalendar(DateTime currentMonth) {
      DateTime firstDayOfCalendar;
      if (currentMonth.getDayOfWeek() == DateTimeConstants.SUNDAY) {
        firstDayOfCalendar = currentMonth;
      } else {
        firstDayOfCalendar = currentMonth.minusDays(currentMonth.getDayOfWeek());
      }
      return firstDayOfCalendar;
    }

    @Autowired
    public void setBeanRepository(BeanRepository beanRepository) {
      this.beanRepository = beanRepository;
    }

    /**
     * For testing only
     */
    void setToday(DateTime today) {
      this.today = today;
    }
  }

}
