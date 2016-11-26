package com.beanlet.web.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BeanletCalendarDay {

  private String dateKey;

  private int year;

  private int month;

  private int dayOfMonth;

  private boolean currentMonth;

  private boolean today;

  private int beanCount;

  public BeanletCalendarDay(String dateKey, int year, int month, int dayOfMonth, boolean currentMonth, boolean today, int beanCount) {
    this.dateKey = dateKey;
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.currentMonth = currentMonth;
    this.today = today;
    this.beanCount = beanCount;
  }

  public String getDateKey() {
    return dateKey;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getDayOfMonth() {
    return dayOfMonth;
  }

  public boolean isCurrentMonth() {
    return currentMonth;
  }

  public boolean isToday() {
    return today;
  }

  public int getBeanCount() {
    return beanCount;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("dateKey", dateKey)
      .append("year", year)
      .append("month", month)
      .append("dayOfMonth", dayOfMonth)
      .append("beanCount", beanCount)
      .append("currentMonth", currentMonth)
      .append("today", today)
      .toString();
  }
}
