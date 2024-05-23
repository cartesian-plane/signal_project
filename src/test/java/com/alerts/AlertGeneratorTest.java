package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("CallToPrintStackTrace")
class AlertGeneratorTest {
  private DataStorage dataStorage;
  private Reader reader;
  private AlertGenerator alertGenerator;
  File resourcesDirectory = new File("src/test/resources");


  @BeforeEach
  void setUp() {
    dataStorage = new DataStorage();
    reader = new Reader();
    alertGenerator = new AlertGenerator(dataStorage);
  }

  @Nested
  @DisplayName("Blood Pressure Data Alerts")
  class BloodPressureAlertEvaluationTest {
    @BeforeEach
    void setUp() {
      // set up the directory for the blood pressure test files
      resourcesDirectory = new File(resourcesDirectory, "alert-mock-data/blood-pressure");
    }
    @Test
    @DisplayName("Critical: Low Diastolic Pressure Alert")
    void testCriticalLowDiastolicPressure() {
      File diastolicPressureData = new File(resourcesDirectory,
          "diastolic-pressure/critical-low-diastolic-pressure.txt");
      correctTimestamps(diastolicPressureData);

      try {
        reader.readData(diastolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "CRITICAL: LOW DIASTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Critical: High Diastolic Pressure Alert")
    void testCriticalHighDiastolicPressure() {
      File diastolicPressureData = new File(resourcesDirectory,
          "diastolic-pressure/critical-high-diastolic-pressure.txt");
      correctTimestamps(diastolicPressureData);

      try {
        reader.readData(diastolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "CRITICAL: HIGH DIASTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Trend: Decreasing Diastolic Pressure Alert")
    void testTrendDecreasingDiastolicPressure() {
      File diastolicPressureData = new File(resourcesDirectory,
          "diastolic-pressure/trend-decreasing-diastolic-pressure.txt");
      correctTimestamps(diastolicPressureData);

      try {
        reader.readData(diastolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "TREND: DECREASING DIASTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Trend: Increasing Diastolic Pressure Alert")
    void testTrendIncreasingDiastolicPressure() {
      File diastolicPressureData = new File(resourcesDirectory,
          "diastolic-pressure/trend-increasing-diastolic-pressure.txt");
      correctTimestamps(diastolicPressureData);

      try {
        reader.readData(diastolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "TREND: INCREASING DIASTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("No Alert: Normal Diastolic Pressure")
    void testNormalDiastolicPressure() {
      File diastolicPressureData = new File(resourcesDirectory,
          "diastolic-pressure/normal-diastolic-pressure.txt");
      correctTimestamps(diastolicPressureData);

      try {
        reader.readData(diastolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertNull(actual);
    }

    @Test
    @DisplayName("Critical: Low Systolic Pressure Alert")
    void testCriticalLowSystolicPressure() {
      File systolicPressureData = new File(resourcesDirectory,
          "systolic-pressure/critical-low-systolic-pressure.txt");
      correctTimestamps(systolicPressureData);

      try {
        reader.readData(systolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "CRITICAL: LOW SYSTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Critical: High Systolic Pressure Alert")
    void testCriticalHighSystolicPressure() {
      File systolicPressureData = new File(resourcesDirectory,
          "systolic-pressure/critical-high-systolic-pressure.txt");
      correctTimestamps(systolicPressureData);

      try {
        reader.readData(systolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "CRITICAL: HIGH SYSTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Trend: Increasing Systolic Pressure Alert")
    void testTrendIncreasingSystolicPressure() {
      File systolicPressureData = new File(resourcesDirectory,
          "systolic-pressure/trend-increasing-systolic-pressure.txt");
      correctTimestamps(systolicPressureData);

      try {
        reader.readData(systolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "TREND: INCREASING SYSTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Trend: Decreasing Systolic Pressure Alert")
    void testTrendDecreasingSystolicPressure() {
      File systolicPressureData = new File(resourcesDirectory,
          "systolic-pressure/trend-decreasing-systolic-pressure.txt");
      correctTimestamps(systolicPressureData);

      try {
        reader.readData(systolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert expected = new Alert(patientId, "TREND: DECREASING SYSTOLIC PRESSURE",
          timestamp);
      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("No Alert: Normal Systolic Pressure")
    void testNormalSystolicPressure() {
      File systolicPressureData = new File(resourcesDirectory,
          "systolic-pressure/normal-systolic-pressure.txt");
      correctTimestamps(systolicPressureData);

      try {
        reader.readData(systolicPressureData, dataStorage);
      } catch (IOException e) {
        System.out.println("Could not read from file");
        e.printStackTrace();
      }

      // patient of interest
      String patientId = String.valueOf(dataStorage.getAllPatients().getFirst().getId());
      List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId));
      long timestamp = records.getFirst().timestamp();
      Patient patient = new Patient(Integer.parseInt(patientId));

      Alert actual = alertGenerator.bloodPressureAlert(patient);
      assertNull(actual);
    }
  }


  /**
   * <p>Helper method only used in a testing context,
   * to convert the timestamps to current time, as the alert evaluation
   * methods only read "current" data (last 10m or so).</p>
   *
   * <p> The conversion still maintains the relative temporal differences, but adjusted for the
   * "current" time.</p>
   *
   * @param source file to correct
   * @throws IOException
   */
  private void correctTimestamps(File source) {
    // This is the cleanest way this team could come up with testing data which depends on timestamps.
    List<String> newLines = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      String line;

      // The offset will be set based on the current time,
      // and then all the timestamps will be adjusted
      long offset = 0;
      boolean isFirstLine = true;
      while ((line = reader.readLine()) != null) {
        Pattern pattern = Pattern.compile("Timestamp: (\\d+)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
          String oldTimestamp = matcher.group(1);

          if (isFirstLine) {
            offset = System.currentTimeMillis() - Long.parseLong(oldTimestamp);
            isFirstLine = false;
          }

          long timestamp = Long.parseLong(oldTimestamp);
          String newTimestamp = String.valueOf(timestamp + offset);
          String newLine = line.replaceFirst(oldTimestamp, newTimestamp);
          newLines.add(newLine);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (PrintWriter writer = new PrintWriter(new FileWriter(source))) {
      for (String line : newLines) {
        writer.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}