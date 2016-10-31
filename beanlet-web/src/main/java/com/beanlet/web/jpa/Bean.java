package com.beanlet.web.jpa;

import org.joda.time.DateTime;

import javax.persistence.Entity;

@Entity
public class Bean extends AbstractEntity<Bean> {

  private EntityId<Beanlet> beanletId;

  private DateTime dateUtc;

  private DateTime dateLocal;

  public EntityId<Beanlet> getBeanletId() {
    return beanletId;
  }

  public void setBeanletId(EntityId<Beanlet> beanletId) {
    this.beanletId = beanletId;
  }

  public DateTime getDateUtc() {
    return dateUtc;
  }

  public void setDateUtc(DateTime dateUtc) {
    this.dateUtc = dateUtc;
  }

  public DateTime getDateLocal() {
    return dateLocal;
  }

  public void setDateLocal(DateTime dateLocal) {
    this.dateLocal = dateLocal;
  }
}
