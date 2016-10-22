package com.beanlet.web.controller;

import com.beanlet.web.jpa.User;
import com.beanlet.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeansController {

  public static final Logger logger = LoggerFactory.getLogger(BeansController.class);

  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String displayBeans(Model model, @AuthenticationPrincipal User user) {
    return "beans";
  }

}
