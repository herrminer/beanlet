package com.beanlet.web.jpa;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RememberMeToken {

  @Id
  private String series;

  private String username;

  private String token;

  private DateTime lastUsed;

  public RememberMeToken() {
    // no-arg constructor
  }

  public RememberMeToken(String series, String username, String token, DateTime lastUsed) {
    this.series = series;
    this.username = username;
    this.token = token;
    this.lastUsed = lastUsed;
  }

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public DateTime getLastUsed() {
    return lastUsed;
  }

  public void setLastUsed(DateTime lastUsed) {
    this.lastUsed = lastUsed;
  }
}
