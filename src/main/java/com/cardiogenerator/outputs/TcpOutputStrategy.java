package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * TcpOutputStrategy implements OutputStrategy interface {@link OutputStrategy} to output data to a
 * TCP socket. It listens for client connections on a specified port and sends data to connected
 * clients.
 */
public class TcpOutputStrategy implements OutputStrategy {

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;

  /**
   * Constructs a TcpOutputStrategy object that listens for client connections on the specified
   * port.
   *
   * @param port The port number on which the TCP server will listen for client connections.
   */
  public TcpOutputStrategy(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("TCP Server started on port " + port);

      // Accept clients in a new thread to not block the main thread
      Executors.newSingleThreadExecutor().submit(() -> {
        try {
          clientSocket = serverSocket.accept();
          out = new PrintWriter(clientSocket.getOutputStream(), true);
          System.out.println("Client connected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Outputs data to the connected client.
   *
   * <p>Data is sent to the PrintWriter, which is initialized with the client's output stream.</p>
   *
   * @param patientId the ID of the patient associated with the data
   * @param timestamp the timestamp of the data
   * @param label     the label associated with the data
   * @param data      the actual data to be output
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    if (out != null) {
      String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
      out.println(message);
    }
  }
}
