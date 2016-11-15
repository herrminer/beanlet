package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SortByTest {

  @Test
  public void testSortByName() throws Exception {
    Beanlet b1 = new Beanlet(null, "a");
    Beanlet b2 = new Beanlet(null, "C");
    Beanlet b3 = new Beanlet(null, "b");
    List<Beanlet> beanlets = new ArrayList<>(3);
    beanlets.add(b1);
    beanlets.add(b2);
    beanlets.add(b3);
    beanlets.sort(SortBy.NAME.getComparator());
    assertThat(beanlets.get(0).getName()).isEqualTo("a");
    assertThat(beanlets.get(1).getName()).isEqualTo("b");
    assertThat(beanlets.get(2).getName()).isEqualTo("C");
  }

  @Test
  public void testSortByBeanCount() throws Exception {
    Beanlet b1 = new Beanlet(); b1.setBeanCount(3);
    Beanlet b2 = new Beanlet(); b2.setBeanCount(1);
    Beanlet b3 = new Beanlet(); b3.setBeanCount(400);
    List<Beanlet> beanlets = new ArrayList<>(3);
    beanlets.add(b1);
    beanlets.add(b2);
    beanlets.add(b3);
    beanlets.sort(SortBy.BEAN_COUNT.getComparator());
    assertThat(beanlets.get(0).getBeanCount()).isEqualTo(400);
    assertThat(beanlets.get(1).getBeanCount()).isEqualTo(3);
    assertThat(beanlets.get(2).getBeanCount()).isEqualTo(1);
  }

  @Test
  public void testSortByDateAdded() throws Exception {
    Beanlet b1 = new Beanlet(); b1.setDateCreated(new DateTime(2016, 2, 1, 0, 0, 0));
    Beanlet b2 = new Beanlet(); b2.setDateCreated(new DateTime(2016, 2, 10, 0, 0, 0));
    Beanlet b3 = new Beanlet(); b3.setDateCreated(new DateTime(2016, 2, 7, 0, 0, 0));
    List<Beanlet> beanlets = new ArrayList<>(3);
    beanlets.add(b1);
    beanlets.add(b2);
    beanlets.add(b3);
    beanlets.sort(SortBy.DATE_ADDED.getComparator());
    assertThat(beanlets.get(0).getDateCreated().getDayOfMonth()).isEqualTo(10);
    assertThat(beanlets.get(1).getDateCreated().getDayOfMonth()).isEqualTo(7);
    assertThat(beanlets.get(2).getDateCreated().getDayOfMonth()).isEqualTo(1);
  }

}