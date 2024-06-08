package com.alerts.decorator;

import com.alerts.IAlert;

public class RepeatedAlertAlertDecorator extends BaseAlertDecorator {
  private int repeatedCount;

  public RepeatedAlertAlertDecorator(IAlert alert) {
    super(alert);
  }

  public void repeatAlert() {
    // magic logic to repeat an alert somehow???
  }

}
