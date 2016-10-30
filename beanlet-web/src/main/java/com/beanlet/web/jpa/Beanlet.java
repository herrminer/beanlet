package com.beanlet.web.jpa;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Beanlet extends AbstractEntity<Beanlet> {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String name;

  private DateTime dateLastLogged;

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

}
