package com.beanlet.web.controller;

import com.beanlet.web.formatter.DateTimeFormatters;
import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BeanController {

  private BeanService beanService;

  @PutMapping("/beanlets/{beanletId}/beans/{beanId}")
  @ResponseBody
  public String updateBean(@PathVariable EntityId<Beanlet> beanletId,
                                       @PathVariable EntityId<Bean> beanId,
                                       @AuthenticationPrincipal User user,
                                       UpdateBeanRequest request) {
    DateTime localDate = DateTimeFormatters.dateTimeSpaced()
      .parseDateTime(request.toString())
      .withZoneRetainFields(request.getTimeZone());
    beanService.modifyBean(user.getId(), beanletId, beanId, localDate);
    return "";
  }

  @DeleteMapping("/beanlets/{beanletId}/beans/{beanId}")
  @ResponseBody
  public String deleteBean(@PathVariable EntityId<Beanlet> beanletId,
                                       @PathVariable EntityId<Bean> beanId,
                                       @AuthenticationPrincipal User user) {
    beanService.deleteBean(user.getId(), beanletId, beanId);
    return "";
  }

  @Autowired
  public void setBeanService(BeanService beanService) {
    this.beanService = beanService;
  }

  public static class UpdateBeanRequest {
    private String date;
    private String hour;
    private String minute;
    private String ampm;
    private DateTimeZone timeZone;

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getHour() {
      return hour;
    }

    public void setHour(String hour) {
      this.hour = hour;
    }

    public String getMinute() {
      return minute;
    }

    public void setMinute(String minute) {
      this.minute = minute;
    }

    public String getAmpm() {
      return ampm;
    }

    public void setAmpm(String ampm) {
      this.ampm = ampm;
    }

    public DateTimeZone getTimeZone() {
      return timeZone;
    }

    public void setTimeZone(DateTimeZone timeZone) {
      this.timeZone = timeZone;
    }

    @Override
    public String toString() {
      return date + " " + hour + " " + minute + " " + ampm;
    }
  }

}
