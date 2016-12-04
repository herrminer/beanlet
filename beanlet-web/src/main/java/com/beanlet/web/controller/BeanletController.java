package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletService;
import com.beanlet.web.service.ModifyBeanletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BeanletController {

  private BeanletService beanletService;

  @GetMapping("/beanlets/{beanletId}")
  public String displayBeanlet(@PathVariable EntityId<Beanlet> beanletId,
                               Model model,
                               @AuthenticationPrincipal User user) {
    model.addAttribute("beanlet", beanletService.getBeanlet(user.getId(), beanletId));
    return "beanlet";
  }

  @PutMapping("/beanlets/{beanletId}")
  @ResponseBody
  public Beanlet modifyBeanlet(@PathVariable EntityId<Beanlet> beanletId,
                              ModifyBeanletRequest request,
                              @AuthenticationPrincipal User user) {
    return beanletService.modifyBeanlet(user.getId(), beanletId, request);
  }

  @DeleteMapping("/beanlets/{beanletId}")
  @ResponseBody
  public Beanlet deleteBeanlet(@PathVariable EntityId<Beanlet> beanletId, @AuthenticationPrincipal User user) {
    return beanletService.deleteBeanlet(user.getId(), beanletId);
  }

  @Autowired
  public void setBeanletService(BeanletService beanletService) {
    this.beanletService = beanletService;
  }
}
