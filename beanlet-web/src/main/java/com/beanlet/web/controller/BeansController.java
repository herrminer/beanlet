package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanService;
import com.beanlet.web.service.BeanletService;
import org.joda.time.DateTime;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BeansController {

  private static final Logger logger = LoggerFactory.getLogger(BeansController.class);

  @Autowired
  private BeanService beanService;

  private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

  @GetMapping("/beanlets/{beanletId}/beans")
  public String displayBeans(@PathVariable EntityId<Beanlet> beanletId,
                             @AuthenticationPrincipal User user,
                             @RequestParam String date,
                             Model model) {
    DateTime dateTime = formatter.parseDateTime(date);
    logger.debug("displayBeans: beanletId: " + beanletId + " and date: " + dateTime);
    model.addAttribute("beans", beanService.getBeansForDate(user.getId(), beanletId, dateTime));
    return "beans";
  }

}
