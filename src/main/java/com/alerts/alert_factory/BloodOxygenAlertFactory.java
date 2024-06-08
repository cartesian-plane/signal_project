package com.alerts.alert_factory;

import com.alerts.Alert;

public class BloodOxygenAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        if (condition.equalsIgnoreCase("LOW_SATURATION")) {
            return new Alert(patientId, "CRITICAL: LOW SATURATION", timestamp);
        } else if (condition.equalsIgnoreCase("RAPID_SATURATION_DROP")) {
            return new Alert(patientId, "TREND: RAPID SATURATION DROP", timestamp);
        }
        return null;
    }
}
