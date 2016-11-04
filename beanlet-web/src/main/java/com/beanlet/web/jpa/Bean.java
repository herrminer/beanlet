package com.beanlet.web.jpa;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.Entity;

@Entity
public class Bean extends AbstractEntity<Bean> {

  private EntityId<Beanlet> beanletId;

  private DateTime utcDate;

  private DateTime localDate;

  private DateTimeZone localTimeZone;

  public EntityId<Beanlet> getBeanletId() {
    return beanletId;
  }

  public void setBeanletId(EntityId<Beanlet> beanletId) {
    this.beanletId = beanletId;
  }

  public DateTime getUtcDate() {
    return utcDate;
  }

  public void setUtcDate(DateTime utcDate) {
    this.utcDate = utcDate;
  }

  public DateTime getLocalDate() {
    return localDate;
  }

  public void setLocalDate(DateTime localDate) {
    this.localDate = localDate;
  }

  public DateTimeZone getLocalTimeZone() {
    return localTimeZone;
  }

  public void setLocalTimeZone(DateTimeZone localTimeZone) {
    this.localTimeZone = localTimeZone;
  }
}
