package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;

public class BeanChangeResponse {
  private EntityId<Beanlet> beanletId;
  private int beanCountForDate;
  private String dateKey;

  BeanChangeResponse(EntityId<Beanlet> beanletId, int beanCountForDate, String dateKey) {
    this.beanletId = beanletId;
    this.beanCountForDate = beanCountForDate;
    this.dateKey = dateKey;
  }

  public EntityId<Beanlet> getBeanletId() {
    return beanletId;
  }

  public int getBeanCountForDate() {
    return beanCountForDate;
  }

  public String getDateKey() {
    return dateKey;
  }
}
