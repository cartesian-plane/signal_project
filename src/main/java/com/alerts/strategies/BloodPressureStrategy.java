package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ThresholdValue;
import com.alerts.alert_factory.AlertFactory;
import com.alerts.alert_factory.BloodPressureAlertFactory;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
  private static final AlertFactory factory = new BloodPressureAlertFactory();
  @Override
  public Alert checkAlert(Patient patient) {

    List<PatientRecord> records = DataStorage.getInstance().getRecords(patient.getId(),
        System.currentTimeMillis() - (10 * 60 * 1000), Long.MAX_VALUE);

    for (int i = 2; i < records.size(); i++) {
      PatientRecord record1 = records.get(i - 2);
      PatientRecord record2 = records.get(i - 1);
      PatientRecord record3 = records.get(i);

      // Critical alert: diastolic & systolic
      if (record1.recordType().equalsIgnoreCase("DiastolicPressure")) {
        if (record1.measurementValue() < 60) {
          return factory.createAlert(patient.getId(),
              "CRITICAL: LOW DIASTOLIC PRESSURE", record1.timestamp());
        } else if (record1.measurementValue() > ThresholdValue.MAX_DIASTOLIC_PRESSURE.getValue()) {
          return factory.createAlert(patient.getId(),
              "CRITICAL: HIGH DIASTOLIC PRESSURE", record1.timestamp());
        }
      } else if (record1.recordType().equalsIgnoreCase("SystolicPressure")) {
        if (record1.measurementValue() < ThresholdValue.MIN_SYSTOLIC_PRESSURE.getValue()) {

          return factory.createAlert(patient.getId(),
              "CRITICAL: LOW SYSTOLIC PRESSURE", record1.timestamp());
        } else if (record1.measurementValue() > ThresholdValue.MAX_SYSTOLIC_PRESSURE.getValue()) {
          return factory.createAlert(patient.getId(),
              "CRITICAL: HIGH SYSTOLIC PRESSURE", record1.timestamp());
        }
      }

      // Trend Alert: Diastolic Pressure
      if (record1.recordType().equals("DiastolicPressure") &&
          record2.recordType().equals("DiastolicPressure") &&
          record3.recordType().equals("DiastolicPressure")) {
        double change1 = record2.measurementValue() - record1.measurementValue();
        double change2 = record3.measurementValue() - record2.measurementValue();

        // use the Signum function to check if the change is strictly monotonous
        if (Math.abs(change1) > ThresholdValue.INCREASING_TREND.getValue() &&
            Math.abs(change2) > ThresholdValue.INCREASING_TREND.getValue() &&
            Math.signum(change1) == Math.signum(change2)) {
          if (Math.signum(change1) > 0) {
            return factory.createAlert(patient.getId(),
                "TREND: INCREASING DIASTOLIC PRESSURE", record1.timestamp());
          } else {
            return factory.createAlert(patient.getId(),
                "TREND: DECREASING DIASTOLIC PRESSURE", record1.timestamp());
          }
        }
      }

      // Trend Alert: Systolic Pressure
      if (record1.recordType().equals("SystolicPressure") &&
          record2.recordType().equals("SystolicPressure") &&
          record3.recordType().equals("SystolicPressure")) {
        double change1 = record2.measurementValue() - record1.measurementValue();
        double change2 = record3.measurementValue() - record2.measurementValue();

        // use the Signum function to check if the change is strictly monotonous
        if (Math.abs(change1) > ThresholdValue.INCREASING_TREND.getValue() &&
            Math.abs(change2) > ThresholdValue.INCREASING_TREND.getValue() &&
            Math.signum(change1) == Math.signum(change2)) {
          if (Math.signum(change1) > 0) {
            return factory.createAlert(patient.getId(),
                "TREND: INCREASING SYSTOLIC PRESSURE", record1.timestamp());
          } else {
            return factory.createAlert(patient.getId(),
                "TREND: DECREASING SYSTOLIC PRESSURE", record1.timestamp());
          }
        }
      }
    }

    return null;
  }
}
