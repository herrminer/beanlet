package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountItController {

  @Autowired
  private BeanletService beanletService;

  @Autowired
  private ConversionService conversionService;

  @PostMapping("/countIt")
  public CountItResponse countIt(@RequestParam EntityId<Beanlet> beanletId,
                        @RequestParam DateTimeZone timeZone,
                        @AuthenticationPrincipal User user) {
    Beanlet beanlet = beanletService.countIt(user.getId(), beanletId, timeZone);
    String formattedLastLogged = conversionService.convert(beanlet.getDateLastLogged(), String.class);
    return new CountItResponse(beanletId, formattedLastLogged, beanlet.getBeanCount());
  }

  static class CountItResponse {
    private EntityId<Beanlet> beanletId;
    private String lastLogged;
    private int beanCount;

    CountItResponse(EntityId<Beanlet> beanletId, String lastLogged, int beanCount) {
      this.beanletId = beanletId;
      this.lastLogged = lastLogged;
      this.beanCount = beanCount;
    }

    public EntityId<Beanlet> getBeanletId() {
      return beanletId;
    }

    public String getLastLogged() {
      return lastLogged;
    }

    public int getBeanCount() {
      return beanCount;
    }
  }

}
