package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ThresholdValue;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {

  @Override
  public Alert checkAlert(Patient patient) {
    // Get all blood oxygen saturation records for the patient
    var dataStorage = DataStorage.getInstance();
    List<PatientRecord> saturationRecords = dataStorage.getRecords(patient.getId(),
            System.currentTimeMillis()
                - (10 * 60 * 1000), System.currentTimeMillis() + 100)
        .stream()
        .filter(record -> record.recordType().equalsIgnoreCase("Saturation"))
        .toList();

    if (saturationRecords.isEmpty()) {
      // No records found within the last 10 minutes
      return null;
    }

    // Get the latest blood saturation record
    PatientRecord latestRecord = saturationRecords.get(0);

    // Check if the latest saturation is below 92%
    if (latestRecord.measurementValue() < ThresholdValue.MIN_SATURATION.getValue()) {
      return new Alert(patient.getId(), "CRITICAL: LOW SATURATION",
          latestRecord.timestamp());
    }

    // Check for rapid drop within a 10-minute interval
    for (int i = 1; i < saturationRecords.size(); i++) {
      PatientRecord prevRecord = saturationRecords.get(i);
      if ((latestRecord.timestamp() - prevRecord.timestamp()) <= (10 * 60 * 1000)) {
        double drop = latestRecord.measurementValue() - prevRecord.measurementValue();
        if (drop >= ThresholdValue.RAPID_DROP.getValue()) {
          return new Alert(patient.getId(), "TREND: RAPID SATURATION DROP",
              latestRecord.timestamp());
        }
      } else {
        // No more records within the 10-minute interval
        break;
      }
    }

    // No alerts triggered
    return null;

  }
}
