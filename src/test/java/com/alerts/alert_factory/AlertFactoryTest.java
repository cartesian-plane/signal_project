package com.alerts.alert_factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;

public class AlertFactoryTest {
    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory bloodPressureFactory = new BloodPressureAlertFactory();

        // Test low systolic pressure alert
        Alert lowSystolicPressureAlert = bloodPressureFactory.createAlert(1, "LOW_SYSTOLIC_PRESSURE", 1624945238000L);
        assertEquals("CRITICAL: LOW SYSTOLIC PRESSURE", lowSystolicPressureAlert.getCondition());

        // Test high systolic pressure alert
        Alert highSystolicPressureAlert = bloodPressureFactory.createAlert(2, "HIGH_SYSTOLIC_PRESSURE", 1624945238000L);
        assertEquals("CRITICAL: HIGH SYSTOLIC PRESSURE", highSystolicPressureAlert.getCondition());

        // Test increasing systolic pressure trend alert
        Alert increasingSystolicPressureAlert = bloodPressureFactory.createAlert(3, "INCREASING_SYSTOLIC_PRESSURE", 1624945238000L);
        assertEquals("TREND: INCREASING SYSTOLIC PRESSURE", increasingSystolicPressureAlert.getCondition());

        // Test decreasing systolic pressure trend alert
        Alert decreasingSystolicPressureAlert = bloodPressureFactory.createAlert(4, "DECREASING_SYSTOLIC_PRESSURE", 1624945238000L);
        assertEquals("TREND: DECREASING SYSTOLIC PRESSURE", decreasingSystolicPressureAlert.getCondition());

        // Test unknown condition
        assertNull(bloodPressureFactory.createAlert(5, "UNKNOWN_CONDITION", 1624945238000L));
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory bloodOxygenFactory = new BloodOxygenAlertFactory();

        // Test low saturation alert
        Alert lowSaturationAlert = bloodOxygenFactory.createAlert(1, "LOW_SATURATION", 1624945238000L);
        assertEquals("CRITICAL: LOW SATURATION", lowSaturationAlert.getCondition());

        // Test rapid saturation drop trend alert
        Alert rapidSaturationDropAlert = bloodOxygenFactory.createAlert(2, "RAPID_SATURATION_DROP", 1624945238000L);
        assertEquals("TREND: RAPID SATURATION DROP", rapidSaturationDropAlert.getCondition());

        // Test unknown condition
        assertNull(bloodOxygenFactory.createAlert(3, "UNKNOWN_CONDITION", 1624945238000L));
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory ecgFactory = new ECGAlertFactory();

        // Test ECG peak alert
        Alert ecgPeakAlert = ecgFactory.createAlert(1, "ECG_PEAK_ALERT", 1624945238000L);
        assertEquals("ECG PEAK ALERT", ecgPeakAlert.getCondition());

        // Test unknown condition
        assertNull(ecgFactory.createAlert(2, "UNKNOWN_CONDITION", 1624945238000L));
    }
}
