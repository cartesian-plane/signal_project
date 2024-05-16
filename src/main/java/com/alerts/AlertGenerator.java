package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data and generating alerts
 * when certain predefined conditions are met. This class relies on a {@link DataStorage} instance
 * to access patient data and evaluate it against specific health criteria.
 */
public class AlertGenerator {

  private DataStorage dataStorage;

  /**
   * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}. The
   * {@code DataStorage} is used to retrieve patient data that this class will monitor and
   * evaluate.
   *
   * @param dataStorage the data storage system that provides access to patient data
   */
  public AlertGenerator(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  /**
   * Evaluates the specified patient's data to determine if any alert conditions are met. If a
   * condition is met, an alert is triggered via the {@link #triggerAlert} method. This method
   * should define the specific conditions under which an alert will be triggered.
   *
   * @param patient the patient data to evaluate for alert conditions
   */
  public void evaluateData(Patient patient) {

    // make a list of all the alert methods, to iterate through them
    List<Function<Patient, Alert>> alertChecks = List.of(
        this::bloodPressureAlert,
        this::bloodSaturationAlert,
        this::hypotensiveHypoxemiaAlert,
        this::ecgAlert
    );

    // iterate through the alert methods, triggering an alert if necessary
    Alert alert;
    for (Function<Patient, Alert> alertCheck : alertChecks) {
      alert = alertCheck.apply(patient);
      if (alert != null) {
        triggerAlert(alert);
      }
    }


  }

  /**
   * Triggers an alert for the monitoring system. This method can be extended to notify medical
   * staff, log the alert, or perform other actions. The method currently assumes that the alert
   * information is fully formed when passed as an argument.
   *
   * @param alert the alert object containing details about the alert condition
   */
  private void triggerAlert(Alert alert) {
    // Implementation might involve logging the alert or notifying staff
  }

  /**
   * Returns an {@link Alert} when the BloodPressureData Alert conditions are met. The patient
   * records considered are the ones within the last 10 minutes.
   *
   * <p>If the conditions are not met, the return type is {@code null}.</p>
   *
   * @param patient patient to check
   * @return {@code Alert} if necessary, {@code null} otherwise
   */
  private Alert bloodPressureAlert(Patient patient) {
    List<PatientRecord> records = dataStorage.getRecords(patient.getId(),
        System.currentTimeMillis() - (10 * 60 * 1000), Long.MAX_VALUE);

    for (int i = 2; i < records.size(); i++) {
      PatientRecord record1 = records.get(i - 2);
      PatientRecord record2 = records.get(i - 1);
      PatientRecord record3 = records.get(i);

      if (record1.recordType().equalsIgnoreCase("DiastolicPressure")) {
        if (exceedsThresholds(60, 120, record1.measurementValue())) {
          return new Alert(String.valueOf(patient.getId()),
              "CRITICAL: DIASTOLIC PRESSURE", record1.timestamp());
        }
      } else if (record1.recordType().equalsIgnoreCase("SystolicPressure")) {
        if (exceedsThresholds(90, 180, record1.measurementValue())) {
          return new Alert(String.valueOf(patient.getId()),
              "CRITICAL: SYSTOLIC PRESSURE", record1.timestamp());
        }
      }

      // Trend Alert: Diastolic Pressure
      if (record1.recordType().equals("DiastolicPressure") &&
          record2.recordType().equals("DiastolicPressure") &&
          record3.recordType().equals("DiastolicPressure")) {
        double change1 = record2.measurementValue() - record1.measurementValue();
        double change2 = record3.measurementValue() - record2.measurementValue();

        // use the Signum function to check if the change is strictly monotonous
        if (Math.abs(change1) > 10 &&
            Math.abs(change2) > 10 &&
            Math.signum(change1) == Math.signum(change2)) {
          return new Alert("TREND: DIASTOLIC PRESSURE",
              String.valueOf(patient.getId()), record1.timestamp());
        }
      }

      // Trend Alert: Systolic Pressure
      if (record1.recordType().equals("SystolicPressure") &&
          record2.recordType().equals("SystolicPressure") &&
          record3.recordType().equals("SystolicPressure")) {
        double change1 = record2.measurementValue() - record1.measurementValue();
        double change2 = record3.measurementValue() - record2.measurementValue();

        // use the Signum function to check if the change is strictly monotonous
        if (Math.abs(change1) > 10 &&
            Math.abs(change2) > 10 &&
            Math.signum(change1) == Math.signum(change2)) {
          return new Alert("TREND: SYSTOLIC PRESSURE",
              String.valueOf(patient.getId()), record1.timestamp());
        }
      }
    }

    return null;
  }

  /**
   * Checks for blood oxygen saturation alerts for the specified patient within the last 10 minutes.
   * Alerts are triggered based on the following conditions:
   * <ul>
   *   <li>Low Saturation Alert: Triggered if the blood oxygen saturation level falls below 92%.</li>
   *   <li>Rapid Drop Alert: Triggered if the blood oxygen saturation level drops by 5% or more
   *       within a 10-minute interval.</li>
   * </ul>
   *
   * @param patient the patient for whom to check for blood oxygen saturation alerts
   * @return a string message indicating the type of alert triggered, or {@code null} if no alerts
   * are triggered within the specified time range
   */
  private Alert bloodSaturationAlert(Patient patient) {
    // Get all blood oxygen saturation records for the patient
    List<PatientRecord> saturationRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("BloodSaturation"))
        .toList();

    if (saturationRecords.isEmpty()) {
      // No records found within the last 10 minutes
      return null;
    }

    // Get the latest blood saturation record
    PatientRecord latestRecord = saturationRecords.get(0);

    // Check if the latest saturation is below 92%
    if (latestRecord.measurementValue() < 92) {
      return new Alert(String.valueOf(patient.getId()), "Low Saturation Alert",
          latestRecord.timestamp());
    }

    // Check for rapid drop within a 10-minute interval
    for (int i = 1; i < saturationRecords.size(); i++) {
      PatientRecord prevRecord = saturationRecords.get(i);
      if ((latestRecord.timestamp() - prevRecord.timestamp()) <= (10 * 60 * 1000)) {
        double drop = latestRecord.measurementValue() - prevRecord.measurementValue();
        if (drop >= 5) {
          return new Alert(String.valueOf(patient.getId()), "Low Saturation Alert",
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

  /**
   * Returns an {@link Alert} when the Hypotensive Hypoxemia Alert conditions are met. The patient
   * records considered are the ones within the last 10 minutes.
   *
   * <p>If the conditions are not met, the return type is {@code null}.</p>
   *
   * @param patient patient to check
   * @return {@code Alert} if necessary, {@code null} otherwise
   */
  private Alert hypotensiveHypoxemiaAlert(Patient patient) {
    List<PatientRecord> bloodPressureRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("BloodPressure"))
        .toList();

    List<PatientRecord> saturationRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("Saturation"))
        .toList();

    for (PatientRecord pressureRecord : bloodPressureRecords) {
      for (PatientRecord oxygenRecord : saturationRecords) {
        if (Math.abs(pressureRecord.timestamp() - oxygenRecord.timestamp()) <= 10 * 60 * 1000) {
          return new Alert(String.valueOf(patient.getId()), "HYPOTENSIVE HYPOXEMIA"
              + " ALERT", Math.max(pressureRecord.timestamp(), oxygenRecord.timestamp()));
        }
      }
    }

    return null;
  }

  private Alert ecgAlert(Patient patient) {
    List<PatientRecord> ecgRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("ECG"))
        .toList();
    return null;
  }

  /**
   * Checks if a value exceeds a given threshold (endpoints included).
   *
   * @param min   the minimum value
   * @param max   the maximum value
   * @param value the value to check
   * @return {@code true} if within exceeds, {@code false} if otherwise
   */
  private boolean exceedsThresholds(double min, double max, double value) {
    if (min >= max) {
      throw new IllegalArgumentException("Min should be strictly less than Max");
    }
    return !(value >= min && value <= max);
  }
}
