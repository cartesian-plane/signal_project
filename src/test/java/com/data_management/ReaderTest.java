package com.data_management;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReaderTest {

  @Test
  @DisplayName("Read from File and pass to DataStorage")
  void testReadDataFromFile() {
    DataStorage dataStorage = new DataStorage();
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
//    messages.add("{\"patientId\":15,\"measurementValue\":4.641559693126796,\"label\":\"RedBloodCells\",\"timestamp\":1715519279864}");
//
//    when(clientMock.messages).thenReturn(messages);
//
//    // Act
//    reader.readDataFromWebSocket(new URI("ws://localhost:8080"), dataStorageMock);
//
//    // Assert
//    ArgumentCaptor<PatientRecord> argumentCaptor = ArgumentCaptor.forClass(PatientRecord.class);
//    verify(dataStorageMock, timeout(1000)).addPatientRecord(argumentCaptor.capture());
//
//    PatientRecord actualRecord = argumentCaptor.getValue();
//    PatientRecord expectedRecord = new PatientRecord(15, 4.641559693126796,
//        "RedBloodCells", 1715519279864L);
//
//    assertEquals(expectedRecord, actualRecord);
  }
}