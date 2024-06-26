package com.cardiogenerator.outputs;

import com.data_management.PatientRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Sets up a WebSocket server that broadcasts generated data to all clients.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

  private WebSocketServer server;
  private ObjectMapper objectMapper;

  public WebSocketOutputStrategy(int port) {
    // create the object mapper used for the output method
    objectMapper = new ObjectMapper();
    server = new SimpleWebSocketServer(new InetSocketAddress(port));
    System.out.println(
        "WebSocket server created on port: " + port + ", listening for connections...");
    server.start();

    // shutdown hook to ensure the websocket is properly closed
    Thread shutdownSocket = new Thread(() -> {
      System.out.println("Stopping WebSocket server...");
      try {
        server.stop();
        System.out.println("Stopped successfully!");
      } catch (InterruptedException e) {
        System.out.println("Error when closing server");
        throw new RuntimeException(e);
      }
    });
    Runtime.getRuntime().addShutdownHook(shutdownSocket);
  }

  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    data = data.replace("%", ""); // clean up the trailing %
    var record = new PatientRecord(patientId, Double.parseDouble(data), label, timestamp);
    String messageJson;
    try {
      // it had to be surrounded in a try-catch, even though the online examples didn't need it
      messageJson = objectMapper.writeValueAsString(record);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    // Broadcast the message to all connected clients
    for (WebSocket conn : server.getConnections()) {
      conn.send(messageJson);
    }
  }

}
