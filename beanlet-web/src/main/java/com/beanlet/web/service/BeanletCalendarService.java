package com.beanlet.web.service;

import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.repository.BeanRepository;
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

    public static final int DAYS_IN_FOUR_WEEK_CALENDAR = 7 * 4;
    public static final int DAYS_IN_FIVE_WEEK_CALENDAR = 7 * 5;
    public static final int DAYS_IN_SIX_WEEK_CALENDAR = 7 * 6;

    private static final  DateTimeFormatter KEY_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd");

    private DateTime todayOverride;

    @Override
    public BeanletCalendar getBeanletCalendar(EntityId<Beanlet> beanletId, int year, int month, DateTimeZone dateTimeZone) {
      logger.debug("getBeanletCalendar: beanletId " + beanletId + ", year: " + year + ", month: " + month + ", timeZone: " + dateTimeZone);

      DateTime today = new DateTime(dateTimeZone).withTime(0, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC);

      // todayOverride may have been set via testing method
      if (todayOverride != null) {
        today = todayOverride;
      }

      logger.debug("getBeanletCalendar: today: " + today);

      DateTime currentMonth = new DateTime(year, month, 1, 0, 0, 0);
      DateTime previousMonth = currentMonth.minusMonths(1);
      DateTime nextMonth = currentMonth.plusMonths(1);

      int daysInCalendar = calculateDaysInCalendar(currentMonth);

      List<BeanletCalendarDay> days = new ArrayList<>(daysInCalendar);
      DateTime day = getFirstDayOfCalendar(currentMonth);
      Map<String, List<Bean>> beans = getBeansMap(beanletId, day, daysInCalendar);

      for (int i=0; i < daysInCalendar; i++){
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

    static int calculateDaysInCalendar(DateTime dateTime) {
      int calendarDaysRequired = dateTime.dayOfMonth().getMaximumValue() +
        (dateTime.getDayOfWeek() == DateTimeConstants.SUNDAY ? 0 : dateTime.getDayOfWeek());
      return ((int) Math.ceil((double) calendarDaysRequired / 7)) * 7;
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
    void setTodayOverride(DateTime todayOverride) {
      this.todayOverride = todayOverride;
    }
  }

}
