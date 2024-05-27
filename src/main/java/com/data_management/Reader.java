package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class Reader implements DataReader {

  @Override
  public void readDataFromWebSocket(URI serverUri, DataStorage dataStorage) {

  }

  @Override
  public void readDataFromFile(File source, DataStorage dataStorage) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      String line = "";

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(", ");
        int patientId = Integer.parseInt(parts[0].split(": ")[1]);
        long timestamp = Long.parseLong(parts[1].split(": ")[1]);
        String label = parts[2].split(": ")[1];
        String measurementValueStr = parts[3].split(": ")[1]
            .replace("%", "").replace("&", "");
        double measurementValue = Double.parseDouble(measurementValueStr);

        dataStorage.addPatientData(patientId, timestamp, label, measurementValue);
      }
    }
  }
}
