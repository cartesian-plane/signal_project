package com.alerts.alert_factory;

import com.alerts.Alert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        if (condition.equalsIgnoreCase("ECG_PEAK_ALERT")) {
            return new Alert(patientId, "ECG PEAK ALERT", timestamp);
        }
        return null;
    }
}
