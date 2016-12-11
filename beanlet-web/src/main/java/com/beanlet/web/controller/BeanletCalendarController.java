package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletCalendar;
import com.beanlet.web.service.BeanletCalendarService;
import com.beanlet.web.service.BeanletService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeanletCalendarController {

  private static final Logger logger = LoggerFactory.getLogger(BeanletCalendarController.class);

  @Autowired
  private BeanletCalendarService beanletCalendarService;

  @GetMapping("/beanlets/{beanletId}/calendar")
  public BeanletCalendar countIt(@PathVariable EntityId<Beanlet> beanletId,
                                 @RequestHeader("X-BNLT-TIMEZONE") DateTimeZone timeZone,
                                 @RequestParam(required = false) Integer year,
                                 @RequestParam(required = false) Integer month,
                                 @AuthenticationPrincipal User user) {
    logger.debug("servicing request for beanlet " + beanletId + " and time zone " + timeZone);

    BeanletCalendar beanletCalendar;

    if (year != null && month != null) {
      beanletCalendar = beanletCalendarService.getBeanletCalendar(beanletId, year, month, timeZone);
    } else if (timeZone != null) {
      DateTime localDateTime = new DateTime(timeZone);
      beanletCalendar = beanletCalendarService.getBeanletCalendar(
        beanletId,
        localDateTime.getYear(),
        localDateTime.getMonthOfYear(),
        timeZone);
    } else {
      beanletCalendar = new BeanletCalendar.Builder().getCalendar();
    }

    return beanletCalendar;
  }

}
