package com.alerts.decorator;

import com.alerts.IAlert;

public class BaseAlertDecorator implements IAlert {
  private IAlert wrapee;

  public BaseAlertDecorator(IAlert alert) {
    this.wrapee = alert;
  }
}
