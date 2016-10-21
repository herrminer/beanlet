package com.beanlet.web.controller;

import com.beanlet.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

  @Autowired
  private UserService userService;

  @GetMapping("/users")
  public String displayUsers(Model model) {
    model.addAttribute("users", userService.getAllUsers());
    return "users";
  }

}
