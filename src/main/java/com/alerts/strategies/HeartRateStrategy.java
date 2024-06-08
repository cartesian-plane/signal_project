package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.alert_factory.AlertFactory;
import com.alerts.alert_factory.BloodPressureAlertFactory;
import com.alerts.alert_factory.ECGAlertFactory;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
  private static final AlertFactory FACTORY = new ECGAlertFactory();
  @Override
  public Alert checkAlert(Patient patient) {

    List<PatientRecord> ecgRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("ECG"))
        .toList();


    double average = ecgRecords.stream()
        .mapToDouble(PatientRecord::measurementValue)
        .average()
        .orElse(0);

    double sumOfSquaredDifferences = ecgRecords.stream()
        .mapToDouble(record -> Math.pow(record.measurementValue() - average, 2))
        .sum();

    double variance = sumOfSquaredDifferences / ecgRecords.size();

    double sd = Math.sqrt(variance);

    double threshold = average + 2 * sd;
    for (PatientRecord record : ecgRecords) {
      if (Math.abs(record.measurementValue()) > threshold) {
        return FACTORY.createAlert(patient.getId(), "ECG PEAK ALERT",
            record.timestamp());
      }
    }

    return null;
  }
}
