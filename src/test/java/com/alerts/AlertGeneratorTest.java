package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.Reader;
import org.junit.jupiter.api.DisplayName;
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

  @Test
  @DisplayName("Critical Low Diastolic Pressure Alert")
  void testCriticalLowDiastolicPressure() {
    DataStorage dataStorage = new DataStorage();
    Reader reader = new Reader();
    AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

    File resourcesDirectory = new File("src/test/resources");
    File diastolicPressureData = new File(resourcesDirectory,
        "alert-mock-data/critical-low-diastolic-pressure.txt");
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
    System.out.println(expected.hashCode());
    Alert actual = alertGenerator.bloodPressureAlert(patient);
    System.out.println(actual.hashCode());

    assertEquals(expected, actual);
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
  private void correctTimestamps(File source){
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
          String timestampStr = matcher.group(1); // group 1 is the first group in the regex, i.e., (\\d+)
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