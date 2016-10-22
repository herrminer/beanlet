package com.beanlet.web.controller;

import com.beanlet.web.jpa.User;
import com.beanlet.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

  public static final Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UserService userService;

  @GetMapping("/users")
  public String displayUsers(Model model, @AuthenticationPrincipal User user) {
    logger.debug("displaying users for user {}", user);
    model.addAttribute("users", userService.getAllUsers());
    return "users";
  }

}
