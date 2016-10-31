package com.beanlet.web;

import java.util.UUID;

public class TestUtils {

  public static String uuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

}
