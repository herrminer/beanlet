package com.beanlet.web.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public interface BeanletCalendarService {

  BeanletCalendar getBeanletCalendar(int year, int month);

  @Service
  class DefaultBeanletCalendarService implements BeanletCalendarService {

    public static final int DAYS_IN_CALENDAR = 35;

    @Override
    public BeanletCalendar getBeanletCalendar(int year, int month) {
      DateTime firstDayOfMonth = new DateTime(year, month, 1, 0, 0, 0);

      DateTime firstDayOfCalendar;

      // should land us on Sunday at the beginning of the calendar
      DateTime currentDay;

      if (firstDayOfMonth.getDayOfWeek() == DateTimeConstants.SUNDAY) {
        firstDayOfCalendar = firstDayOfMonth;
      } else {
        firstDayOfCalendar = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek());
      }

      List<BeanletCalendarDay> days = new ArrayList<>(DAYS_IN_CALENDAR);

      for (int i=0; i < DAYS_IN_CALENDAR; i++){
        currentDay = firstDayOfCalendar.plusDays(i);
        days.add(new BeanletCalendarDay(
          currentDay.getYear(),
          currentDay.getMonthOfYear(),
          currentDay.getDayOfMonth(),
          currentDay.getMonthOfYear() == month,
          0));
      }

      DateTime previousMonth = firstDayOfMonth.minusMonths(1);
      DateTime nextMonth = firstDayOfMonth.plusMonths(1);
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
  }

}
