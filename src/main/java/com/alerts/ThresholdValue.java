package com.alerts;

public enum ThresholdValue {
  MIN_SYSTOLIC_PRESSURE(90),
  MAX_SYSTOLIC_PRESSURE(180),
  MIN_DIASTOLIC_PRESSURE(60),
  MAX_DIASTOLIC_PRESSURE(120),
  INCREASING_TREND(10),
  DECREASING_TREND(10),
  MIN_SATURATION(92),
  RAPID_DROP(5);

  private final double value;

  ThresholdValue(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }

}
