package com.data_management;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single record of patient data at a specific point in time. This class stores all
 * necessary details for a single observation or measurement taken from a patient, including the
 * type of record (such as ECG, blood pressure), the measurement value, and the exact timestamp when
 * the measurement was taken.
 *
 * @param patientId        the unique identifier for the patient
 * @param recordType       the type of measurement (e.g., "ECG", "Blood Pressure")
 * @param measurementValue the numerical value of the recorded measurement
 * @param timestamp        the time at which the measurement was recorded, in milliseconds since
 *                         epoch
 */
public record PatientRecord(
    @JsonProperty("patientId") int patientId,
    @JsonProperty("measurementValue") double measurementValue,
    @JsonProperty("recordType") String recordType,
    @JsonProperty("timestamp") long timestamp) {}
