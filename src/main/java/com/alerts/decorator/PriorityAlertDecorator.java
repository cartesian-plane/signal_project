package com.alerts.decorator;

import com.alerts.IAlert;

public class PriorityAlertDecorator extends BaseAlertDecorator{
  private AlertSeverity severity;
  public PriorityAlertDecorator(IAlert alert) {
    super(alert);
  }

  public void setSeverity(AlertSeverity severity) {
    this.severity = severity;
  }
}
