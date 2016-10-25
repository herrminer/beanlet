package com.beanlet.web;

import org.junit.Test;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.*;

/**
 * Running Beanlet in UTC is VERY important.
 * Thus this test to ensure it's correctly set.
 */
public class TimeZoneTest {

  @Test
  public void testDefaultTimezoneIsUtc() {
    assertThat(TimeZone.getDefault().getID()).isEqualTo("UTC");
  }

}
