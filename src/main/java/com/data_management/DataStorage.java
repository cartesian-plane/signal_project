package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring system. This class
 * serves as a repository for all patient records, organized by patient IDs.
 */
public class DataStorage {

  private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.


  /**
   * The main method for the DataStorage class. Initializes the system, reads data into storage, and
   * continuously monitors and evaluates patient data.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    // DataReader is not defined in this scope, should be initialized appropriately.
    // DataReader reader = new SomeDataReaderImplementation("path/to/data");
    DataStorage storage = new DataStorage();

    // Assuming the reader has been properly initialized and can read data into the
    // storage
    // reader.readData(storage);

    // Example of using DataStorage to retrieve and print records for a patient
    List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
    for (PatientRecord record : records) {
      System.out.println("Record for Patient ID: " + record.patientId() +
          ", Type: " + record.recordType() +
          ", Data: " + record.measurementValue() +
          ", Timestamp: " + record.timestamp());
    }

    // Initialize the AlertGenerator with the storage
    AlertGenerator alertGenerator = new AlertGenerator(storage);

    // Evaluate all patients' data to check for conditions that may trigger alerts
    for (Patient patient : storage.getAllPatients()) {
      alertGenerator.evaluateData(patient);
    }
  }

  /**
   * Constructs a new instance of DataStorage, initializing the underlying storage structure.
   */
  public DataStorage() {
    this.patientMap = new HashMap<>();
  }

  /**
   * Adds or updates patient data in the storage. If the patient does not exist, a new Patient
   * object is created and added to the storage. Otherwise, the new data is added to the existing
   * patient's records.
   *
   * @param patientId        the unique identifier of the patient
   * @param timestamp        the time at which the measurement was taken, in milliseconds since the
   *                         Unix epoch
   * @param recordType       the type of record, e.g., "HeartRate", "BloodPressure"
   * @param measurementValue the value of the health metric being recorded
   */
  @Deprecated
  public void addPatientData(int patientId, long timestamp, String recordType,
      double measurementValue) {
    Patient patient = patientMap.computeIfAbsent(patientId, Patient::new);
    patient.addRecord(measurementValue, recordType, timestamp);
  }

  /**
   * Adds or updates a {@link PatientRecord} in storage.
   * <p>If the patient does not exist, a new Patient object is created and added to the storage.
   *   Otherwise, the new data is added to the existing patient's records.</p>
   *
   * @param record containing the necessary information
   */
  public void addPatientRecord(PatientRecord record) {
    //TODO make this method replace the addPatientData(), if the time allows for refactoring
    int patientId = record.patientId();
    Patient patient = patientMap.computeIfAbsent(patientId, Patient::new);
    patient.addPatientData(record);
  }

  /**
   * Retrieves a list of PatientRecord objects for a specific patient, filtered by a time range.
   *
   * @param patientId the unique identifier of the patient whose records are to be retrieved
   * @param startTime the start of the time range, in milliseconds since the Unix epoch
   * @param endTime   the end of the time range, in milliseconds since the Unix epoch
   * @return a list of PatientRecord objects that fall within the specified time range
   */
  public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
    Patient patient = patientMap.get(patientId);
    if (patient != null) {
      return patient.getRecords(startTime, endTime);
    }
    return new ArrayList<>();
  }

  /**
   * <p>Retrieves a complete list of PatientRecord objects for a specific patient
   * (not filtered for a time range).</p>
   *
   * @param patientId the unique identifier of the patient whose records are to be retrieved
   * @return a list of PatientRecord objects that fall within the specified time range
   */
  public List<PatientRecord> getRecords(int patientId) {
    Patient patient = patientMap.get(patientId);
    if (patient != null) {
      return patient.getRecords();
    }
    return new ArrayList<>(); // return an empty list if no patient is found
  }

  /**
   * Retrieves a collection of all patients stored in the data storage.
   *
   * @return a list of all patients
   */
  public List<Patient> getAllPatients() {
    return new ArrayList<>(patientMap.values());
  }


}
