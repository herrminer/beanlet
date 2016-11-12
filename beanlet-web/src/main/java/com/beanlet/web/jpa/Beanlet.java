package com.beanlet.web.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Beanlet extends AbstractEntity<Beanlet> {

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String name;

  private DateTime dateLastLogged;

  private int beanCount;

  private int sortOrder;

  public Beanlet() {
    // no-arg constructor
  }

  public Beanlet(User user, String name) {
    this.user = user;
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DateTime getDateLastLogged() {
    return dateLastLogged;
  }

  public void setDateLastLogged(DateTime dateLastLogged) {
    this.dateLastLogged = dateLastLogged;
  }

  public int getBeanCount() {
    return beanCount;
  }

  public void setBeanCount(int beanCount) {
    this.beanCount = beanCount;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }
}
