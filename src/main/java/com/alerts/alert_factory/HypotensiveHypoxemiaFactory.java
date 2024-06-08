package com.alerts.alert_factory;

import com.alerts.Alert;

public class HypotensiveHypoxemiaFactory extends AlertFactory{
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
      return new Alert(patientId, condition, timestamp);
    }
}
