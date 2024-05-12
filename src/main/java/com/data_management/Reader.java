package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader implements DataReader {

  @Override
  public void readData(File source, DataStorage dataStorage) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      String line = "";

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(", ");
        int patientId = Integer.parseInt(parts[0].split(": ")[1]);
        long timestamp = Long.parseLong(parts[1].split(": ")[1]);
        String label = parts[2].split(": ")[1];
        double measurementValue = Double.parseDouble(parts[3].split(": ")[1]);

        dataStorage.addPatientData(patientId, timestamp, label, measurementValue);
      }
    }
  }
}
