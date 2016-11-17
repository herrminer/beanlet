package com.beanlet.web.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class BeanletCalendar {

  private int previousYear;

  private int previousMonth;

  private int year;

  private int month;

  private int nextYear;

  private int nextMonth;

  List<BeanletCalendarDay> days;

  private BeanletCalendar() {}

  public int getPreviousYear() {
    return previousYear;
  }

  public int getPreviousMonth() {
    return previousMonth;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getNextYear() {
    return nextYear;
  }

  public int getNextMonth() {
    return nextMonth;
  }

  public List<BeanletCalendarDay> getDays() {
    return days;
  }

  public static class Builder {
    private BeanletCalendar calendar = new BeanletCalendar();
    public Builder withYear(int year) { calendar.year = year; return this; }
    public Builder withMonth(int month) { calendar.month = month; return this; }
    public Builder withPreviousYear(int previousYear) { calendar.previousYear = previousYear; return this; }
    public Builder withPreviousMonth(int previousMonth) { calendar.previousMonth = previousMonth; return this; }
    public Builder withNextYear(int nextYear) { calendar.nextYear = nextYear; return this; }
    public Builder withNextMonth(int nextMonth) { calendar.nextMonth = nextMonth; return this; }
    public Builder withDays(List<BeanletCalendarDay> days) { calendar.days = days; return this; }
    public BeanletCalendar getCalendar() { return calendar; }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("year", year)
      .append("month", month)
      .toString();
  }
}
