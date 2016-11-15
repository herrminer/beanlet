package com.beanlet.web.service;

import com.beanlet.web.jpa.Beanlet;

import java.util.Collections;
import java.util.Comparator;

public enum SortBy {
  NAME("name", (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()), false),
  BEAN_COUNT("beans", (o1, o2) -> o1.getBeanCount() > o2.getBeanCount() ? 1 : o1.getBeanCount() < o2.getBeanCount() ? -1 : 0, true),
  DATE_ADDED("added", (o1, o2) -> o1.getDateCreated().compareTo(o2.getDateCreated()), true);

  private String name;
  private Comparator<? super Beanlet> comparator;

  SortBy(String name, Comparator<? super Beanlet> comparator, boolean reverse) {
    this.name = name;
    this.comparator = reverse ? Collections.reverseOrder(comparator) : comparator;
  }

  public String getName() {
    return name;
  }

  public Comparator<? super Beanlet> getComparator() {
    return comparator;
  }

  public static SortBy fromName(String name) {
    for (SortBy sortBy : values()) {
      if (sortBy.getName().equals(name))
        return sortBy;
    }
    throw new IllegalArgumentException("no SortBy found for name: " + name);
  }
}
