package com.beanlet.web.controller;

import com.beanlet.web.jpa.Beanlet;
import com.beanlet.web.jpa.EntityId;
import com.beanlet.web.jpa.User;
import com.beanlet.web.service.BeanletService;
import com.beanlet.web.service.SortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SortBeanletsController {

  @Autowired
  private BeanletService beanletService;

  @Autowired
  private ConversionService conversionService;

  @PostMapping("/beanlets/sort")
  public List<EntityId<Beanlet>> sortBeanlets(@RequestParam String sortBy, @AuthenticationPrincipal User user) {
    return beanletService.sortBeanlets(user.getId(), SortBy.fromName(sortBy));
  }

}
