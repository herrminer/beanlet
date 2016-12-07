package com.beanlet.web.controller;

import com.beanlet.web.formatter.DateTimeFormatters;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.beanlet.web.formatter.DateTimeFormatters.dateKeyFormatter;

@Controller
public class BeansController {

  private static final Logger logger = LoggerFactory.getLogger(BeansController.class);

  private BeanService beanService;

  @GetMapping("/beanlets/{beanletId}/beans")
  public String displayBeans(@PathVariable EntityId<Beanlet> beanletId,
                             @AuthenticationPrincipal User user,
                             @RequestParam String dateKey,
                             Model model) {
    DateTime dateTime = dateKeyFormatter().parseDateTime(dateKey);
    logger.debug("displayBeans: beanletId: " + beanletId + " and date: " + dateTime);
    model.addAttribute("beans", beanService.getBeansForDate(user.getId(), beanletId, dateTime));
    return "beans";
  }

  @PostMapping("/beanlets/{beanletId}/beans")
  @ResponseBody
  public String addBeanForDate(@PathVariable EntityId<Beanlet> beanletId,
                                           @AuthenticationPrincipal User user,
                                           @RequestParam String dateKey,
                                           @RequestParam DateTimeZone timeZone) {
    DateTime dateTime = dateKeyFormatter().parseDateTime(dateKey).withZoneRetainFields(timeZone).withTime(12, 0, 0, 0); // default to noon
    logger.debug("addBeanForDate: beanletId: " + beanletId + " and date: " + dateTime);
    beanService.addBean(user.getId(), beanletId, dateTime);
    return "";
  }

  @Autowired
  public void setBeanService(BeanService beanService) {
    this.beanService = beanService;
  }
}
