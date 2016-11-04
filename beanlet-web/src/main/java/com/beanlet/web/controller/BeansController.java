package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanService;
import com.beanlet.web.service.BeanletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BeansController {

  @Autowired
  private BeanService beanService;

  @GetMapping("/beanlets/{beanletId}/beans")
  public String displayBeans(@PathVariable EntityId<Beanlet> beanletId,
                             Model model,
                             @AuthenticationPrincipal User user) {
    model.addAttribute("beans", beanService.getBeans(user.getId(), beanletId));
    return "beans";
  }

}
