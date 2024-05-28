package com.data_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

public class Reader implements DataReader {

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Creates a thread to constantly process messages from a {@code WebSocketServer}
   * and stores them.
   *
   * @param serverUri the URI of the server (i.e. {@code ws://localhost:8080})
   * @param dataStorage the storage to write to
   */
  @Override
  public void readDataFromWebSocket(URI serverUri, DataStorage dataStorage) {
    var client = new SimpleWebSocketClient(serverUri);
    client.connect();

    //TODO add some "error handling"
    Thread readData = new Thread(() -> {
      while (true) {
        String message = client.messages.poll();
        if (message != null) {
          try {
            var record = objectMapper.readValue(message, PatientRecord.class);
            dataStorage.addPatientRecord(record);
          } catch (IOException e) {
            System.out.println("Could not parse JSON");
            throw new UncheckedIOException(e);
          }
        }
      }
    });
    readData.start();


  }

  @Override
  public void readDataFromFile(File source, DataStorage dataStorage) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
      String line;

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
