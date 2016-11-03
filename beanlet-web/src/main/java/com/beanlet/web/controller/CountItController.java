package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountItController {

  @Autowired
  private BeanletService beanletService;

  @PostMapping("/countIt")
  public Beanlet countIt(@RequestParam EntityId<Beanlet> beanletId, @AuthenticationPrincipal User user) {
    return beanletService.countIt(user.getId(), beanletId);
  }

}
