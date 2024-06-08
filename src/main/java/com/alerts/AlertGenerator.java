package com.alerts;

import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.HypotensiveHypoxemiaStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;
import java.util.function.Function;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data and generating alerts
 * when certain predefined conditions are met. This class relies on a {@link DataStorage} instance
 * to access patient data and evaluate it against specific health criteria.
 */
public class AlertGenerator {

  private DataStorage dataStorage;
  private final double MIN_SYSTOLIC_PRESSURE = 90;
  private final double MIN_SPO2 = 92;


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
        this::hypotensiveHypoxemiaAlert,
        this::bloodPressureAlert,
        this::bloodSaturationAlert,
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
    // will complete this when building the alert dispatch system
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
  public Alert bloodPressureAlert(Patient patient) {
    AlertStrategy strategy = new BloodPressureStrategy();
    return strategy.checkAlert(patient);
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
  public Alert bloodSaturationAlert(Patient patient) {
    AlertStrategy strategy = new OxygenSaturationStrategy();
    return strategy.checkAlert(patient);
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
  public Alert hypotensiveHypoxemiaAlert(Patient patient) {
    AlertStrategy strategy = new HypotensiveHypoxemiaStrategy();
    return strategy.checkAlert(patient);
  }

  /**
   * @param patient
   * @return
   */
  public Alert ecgAlert(Patient patient) {
    AlertStrategy strategy = new HeartRateStrategy();
    return strategy.checkAlert(patient);
  }

  /**
   * Returns an {@link Alert} based on the patient Alerts generated from the
   * {@link com.cardiogenerator.HealthDataSimulator}.
   *
   * @param patient patient to check
   * @return {@code Alert} if necessary, {@code null} otherwise
   */
  public Alert triggeredPatientAlert(Patient patient) {
    List<PatientRecord> alertRecords = patient.getRecords(System.currentTimeMillis()
            - (10 * 60 * 1000), System.currentTimeMillis())
        .stream()
        .filter(record -> record.recordType().equals("Alert"))
        .toList();

    for (PatientRecord alert : alertRecords) {
      // alerts don't have numerical values, so this makes no sense
      // the whole code needs to be changed
    }
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
  @Deprecated
  private boolean exceedsThresholds(double min, double max, double value) {
    if (min >= max) {
      throw new IllegalArgumentException("Min should be strictly less than Max");
    }
    return !(value >= min && value <= max);
  }
}
