package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BeanletController {

  @GetMapping("/beanlets/{beanletId}")
  public String displayBeanlet(@PathVariable EntityId<Beanlet> beanletId,
                               Model model,
                               @AuthenticationPrincipal User user) {
    return "beanlet";
  }

}
