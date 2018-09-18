package org.ufla.dcc.equivalencyanalyse.model.entity;

import java.io.Serializable;

public enum Difficulty implements Serializable {

  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

  private int value;

  Difficulty(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
