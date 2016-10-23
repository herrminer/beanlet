package com.beanlet.web.controller;

import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeanletsController {

  @Autowired
  private BeanletService beanletService;

  @GetMapping("/beanlets")
  public String displayBeans(Model model, @AuthenticationPrincipal User user) {
    model.addAttribute("beanlets", beanletService.getBeanletsForUserId(user.getId()));
    return "beanlets";
  }

}
