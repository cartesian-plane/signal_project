package com.data_management;

/**
 * Represents a single record of patient data at a specific point in time. This class stores all
 * necessary details for a single observation or measurement taken from a patient, including the
 * type of record (such as ECG, blood pressure), the measurement value, and the exact timestamp when
 * the measurement was taken.
 *
 * @param patientId        the unique identifier for the patient
 * @param recordType       the type of measurement (e.g., "ECG", "Blood Pressure")
 * @param measurementValue the numerical value of the recorded measurement
 * @param timestamp         the time at which the measurement was recorded,
 *                         in milliseconds since epoch
 */
public record PatientRecord(int patientId, double measurementValue, String recordType,
                            long timestamp) {


  /**
   * Returns the patient ID associated with this record.
   *
   * @return the patient ID
   */
  @Override
  public int patientId() {
    return patientId;
  }

  /**
   * Returns the measurement value of this record.
   *
   * @return the measurement value
   */
  @Override
  public double measurementValue() {
    return measurementValue;
  }

  /**
   * Returns the timestamp when this record was taken.
   *
   * @return the timestamp in milliseconds since epoch
   */
  @Override
  public long timestamp() {
    return timestamp;
  }

  /**
   * Returns the type of record (e.g., "ECG", "Blood Pressure").
   *
   * @return the record type
   */
  @Override
  public String recordType() {
    return recordType;
  }
}
