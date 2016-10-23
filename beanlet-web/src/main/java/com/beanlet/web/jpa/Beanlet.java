package com.beanlet.web.jpa;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

@Entity
public class Beanlet {

  @Id
  @GeneratedValue
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String name;

  private DateTime dateCreated;

  private DateTime dateUpdated;

  private DateTime dateLastLogged;

  @Version
  private Integer version;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public DateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(DateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public DateTime getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(DateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public DateTime getDateLastLogged() {
    return dateLastLogged;
  }

  public void setDateLastLogged(DateTime dateLastLogged) {
    this.dateLastLogged = dateLastLogged;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @PrePersist
  void prepersist() {
    dateCreated = new DateTime();
    dateUpdated = new DateTime();
  }

  @PreUpdate
  void preupdate() {
    dateUpdated = new DateTime();
  }
}
