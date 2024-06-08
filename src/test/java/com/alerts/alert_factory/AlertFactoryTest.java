package com.alerts.alert_factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;

public class AlertFactoryTest {
    @Test
    void testBloodPressureAlert() {
        // Arrange
        AlertFactory factory = new BloodPressureAlertFactory();
        int patientId = 12345;
        String condition = "HIGH_BLOOD_PRESSURE";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testBloodPressureAlertWithDifferentCondition() {
        // Arrange
        AlertFactory factory = new BloodPressureAlertFactory();
        int patientId = 12345;
        String condition = "LOW_BLOOD_PRESSURE";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testBloodOxygenAlert() {
        // Arrange
        AlertFactory factory = new BloodOxygenAlertFactory();
        int patientId = 13579;
        String condition = "LOW_OXYGEN_LEVEL";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testBloodOxygenAlertWithDifferentCondition() {
        // Arrange
        AlertFactory factory = new BloodOxygenAlertFactory();
        int patientId = 13579;
        String condition = "HIGH_OXYGEN_LEVEL";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testECGAlert() {
        // Arrange
        AlertFactory factory = new ECGAlertFactory();
        int patientId = 67890;
        String condition = "IRREGULAR_HEART_RATE";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testECGAlertWithDifferentCondition() {
        // Arrange
        AlertFactory factory = new ECGAlertFactory();
        int patientId = 67890;
        String condition = "NORMAL_HEART_RATE";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testHypotensiveHypoxemiaAlert() {
        // Arrange
        AlertFactory factory = new HypotensiveHypoxemiaFactory();
        int patientId = 12345;
        String condition = "HYPOTENSIVE_HYPOXEMIA";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }

    @Test
    void testHypotensiveHypoxemiaAlertWithDifferentCondition() {
        // Arrange
        AlertFactory factory = new HypotensiveHypoxemiaFactory();
        int patientId = 12345;
        String condition = "ANOTHER_CONDITION";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }
}
