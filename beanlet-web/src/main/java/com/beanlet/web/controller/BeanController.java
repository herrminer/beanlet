package com.beanlet.web.controller;

import com.beanlet.web.formatter.DateTimeFormatters;
import com.beanlet.web.jpa.Bean;
import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BeanController {

  private BeanService beanService;

  @DeleteMapping("/beanlets/{beanletId}/beans/{beanId}")
  @ResponseBody
  public BeanChangeResponse deleteBean(@PathVariable EntityId<Beanlet> beanletId,
                                       @PathVariable EntityId<Bean> beanId,
                                       @AuthenticationPrincipal User user) {
    Bean bean = beanService.deleteBean(user.getId(), beanletId, beanId);
    String dateKey = DateTimeFormatters.dateKeyFormatter().print(bean.getLocalDate());
    int beanCount = beanService.getBeansForDate(user.getId(), beanletId, bean.getLocalDate()).size();
    return new BeanChangeResponse(beanletId, beanCount, dateKey);
  }

  @Autowired
  public void setBeanService(BeanService beanService) {
    this.beanService = beanService;
  }
}
