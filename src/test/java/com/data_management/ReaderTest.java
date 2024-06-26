package com.data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.cardiogenerator.outputs.SimpleWebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ReaderTest {

  @Test
  @DisplayName("Read from File and pass to DataStorage")
  void testReadDataFromFile() {
    DataStorage dataStorage = DataStorage.getInstance();
    Reader reader = new Reader();

    // resources directory for test files
    File resourcesDirectory = new File("src/test/resources");

    File testFile = new File(resourcesDirectory, "RedBloodCells.txt");

    try {
      reader.readDataFromFile(testFile, dataStorage);

      // the expected values were manually copied from the text file
      var firstPatientRecord = dataStorage.getRecords(15, 1,
          1715519279865L).get(0);
      var expectedFirstPatientRecord = new PatientRecord(15,
          4.641559693126796, "RedBloodCells", 1715519279864L);
      // if the 2 records match, then the parsing and transfer is done correctly

      assertEquals(expectedFirstPatientRecord, firstPatientRecord);

      var secondPatientRecord = dataStorage.getRecords(62, 0,
          1715519279864L).get(0);
      var expectedSecondPatientRecord = new PatientRecord(62,
          5.757744563214141, "RedBloodCells", 1715519279864L);

      assertEquals(expectedSecondPatientRecord, secondPatientRecord);

    } catch (IOException e) {
      System.err.println("Error reading from file: " + testFile);
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Get data from WebsocketClient and pass to DataStorage")
  void readDataFromWebSocket() {
//    SimpleWebSocketClient clientMock = mock(SimpleWebSocketClient.class);
//    DataStorage dataStorageMock = mock(DataStorage.class);
//    Reader reader = new Reader();
//
//    LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>();
//    messages.add("{\"patientId\":15,\"measurementValue\":4.641559693126796,\"label\":"
//        + "\"RedBloodCells\",\"timestamp\":1715519279864}");
//
//     when(clientMock.getMessages()).thenReturn(messages);
//
//    URI uri;
//    try {
//      uri = new URI("ws://localhost:8080");
//    } catch (URISyntaxException e) {
//      throw new RuntimeException(e);
//    }
//    reader.readDataFromWebSocket(uri, dataStorageMock);
//
//
//    ArgumentCaptor<PatientRecord> argumentCaptor = ArgumentCaptor.forClass(PatientRecord.class);
//    verify(dataStorageMock, timeout(1000)).addPatientRecord(argumentCaptor.capture());
//
//    PatientRecord actualRecord = argumentCaptor.getValue();
//    PatientRecord expectedRecord = new PatientRecord(15, 4.641559693126796,
//        "RedBloodCells", 1715519279864L);
//
//    assertEquals(expectedRecord, actualRecord);
  }


  @Nested
  @DisplayName("Integration tests WebsocketClient->Reader->DataStorage->AlertGenerator")
  class IntegrationTest {
    private SimpleWebSocketServer server;

    // simple methods to set up and shut down the WebSocketServer
    @BeforeEach
    void setUp() {
      server = new SimpleWebSocketServer(new InetSocketAddress(8080));
      server.start();
    }

    @AfterEach
    void tearDown() {
      try {
        System.out.println("Stopping server...");
        server.stop();
        System.out.println("Server stopped successfully!");
      } catch (InterruptedException e) {
        System.out.println("Error when stopping server");
        throw new RuntimeException(e);
      }
    }

    @Test
    void testWebSocketIntegration() {
      // had to move the WebSocketServer into its separate class, in order to load it

      DataStorage dataStorage = DataStorage.getInstance();
      dataStorage.clearData();
      AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
      Reader reader = new Reader();
      URI serverUri = null;
      try {
        serverUri = new URI("ws://localhost:8080");
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }

      reader.readDataFromWebSocket(serverUri, dataStorage);

      long timestamp = System.currentTimeMillis();
      String message1 = "{\"patientId\":15,\"measurementValue\":181,\"recordType\":\"SystolicPressure\",\"timestamp\":"+timestamp+"}";
      String message2 = "{\"patientId\":15,\"measurementValue\":181,\"recordType\":\"SystolicPressure\",\"timestamp\":"+(timestamp+1)+"}";
      String message3 = "{\"patientId\":15,\"measurementValue\":181,\"recordType\":\"SystolicPressure\",\"timestamp\":"+(timestamp+2)+"}";

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      for (WebSocket conn : server.getConnections()) {
        System.out.println("sending messages");
        conn.send(message1);
        conn.send(message2);
        conn.send(message3);
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }



      // timestamp to use for testing purposes

      PatientRecord expectedRecord = new PatientRecord(15, 181, "SystolicPressure", timestamp);
      PatientRecord actualRecord = dataStorage.getRecords(15).get(0);

      assertEquals(expectedRecord, actualRecord);

      Patient testPatient = new Patient(15);
      var expectedAlert = new Alert(15,
          "CRITICAL: HIGH SYSTOLIC PRESSURE" ,timestamp);
      var actualAlert = alertGenerator.bloodPressureAlert(testPatient);
      assertEquals(expectedAlert, actualAlert);
    }

  }

}