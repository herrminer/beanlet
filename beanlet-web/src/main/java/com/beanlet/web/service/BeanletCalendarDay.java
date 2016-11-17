package com.beanlet.web.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BeanletCalendarDay {

  private boolean currentMonth;

  private int year;

  private int month;

  private int dayOfMonth;

  private int beanCount;

  public BeanletCalendarDay(int year, int month, int dayOfMonth, boolean currentMonth, int beanCount) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.currentMonth = currentMonth;
    this.beanCount = beanCount;
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

  public int getBeanCount() {
    return beanCount;
  }

  public boolean isCurrentMonth() {
    return currentMonth;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("year", year)
      .append("month", month)
      .append("dayOfMonth", dayOfMonth)
      .append("beanCount", beanCount)
      .append("currentMonth", currentMonth)
      .toString();
  }
}
