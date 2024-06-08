package com.alerts.alert_factory;

import com.alerts.Alert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        if (condition.equalsIgnoreCase("LOW_SYSTOLIC_PRESSURE")) {
            return new Alert(patientId, "CRITICAL: LOW SYSTOLIC PRESSURE", timestamp);
        } else if (condition.equalsIgnoreCase("HIGH_SYSTOLIC_PRESSURE")) {
            return new Alert(patientId, "CRITICAL: HIGH SYSTOLIC PRESSURE", timestamp);
        } else if (condition.equalsIgnoreCase("INCREASING_SYSTOLIC_PRESSURE")) {
            return new Alert(patientId, "TREND: INCREASING SYSTOLIC PRESSURE", timestamp);
        } else if (condition.equalsIgnoreCase("DECREASING_SYSTOLIC_PRESSURE")) {
            return new Alert(patientId, "TREND: DECREASING SYSTOLIC PRESSURE", timestamp);
        }
        return null;
    }
}
