package com.data_management;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single record of patient data at a specific point in time. This class stores all
 * necessary details for a single observation or measurement taken from a patient, including the
 * type of record (such as ECG, blood pressure), the measurement value, and the exact timestamp when
 * the measurement was taken.
 *
 * <p><i>Note: The "Alert" record type has its measurement values converted to numbers,
 * for simplicity; {@code 1} means <b>triggered</b>, {@code 0} means <b>resolved</b></i></p>
 *
 * @param patientId        the unique identifier for the patient
 * @param recordType       the type of measurement (e.g., "ECG", "Blood Pressure")
 * @param measurementValue the numerical value of the recorded measurement <i>(in the case of the
 *                         "Alert" record type, {@code 1} means <b>triggered</b>, and {@code 0} means
 *                         <b>resolved</b>)</i>
 * @param timestamp        the time at which the measurement was recorded, in milliseconds since
 *                         epoch
 */
public record PatientRecord(
    @JsonProperty("patientId") int patientId,
    @JsonProperty("measurementValue") double measurementValue,
    @JsonProperty("recordType") String recordType,
    @JsonProperty("timestamp") long timestamp) {}
