package com.data_management;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.java_websocket.server.WebSocketServer;

public interface DataReader {

  /**
   * Reads data from a given {@link WebSocketServer} and stores it in the data storage.
   * This allows for more efficient real-time data transfers.
   * @param serverUri the URI of the server (i.e. {@code ws://localhost:8080})
   * @param dataStorage the storage to write to
   */
  void readDataFromWebSocket(URI serverUri, DataStorage dataStorage);
  /**
   * Reads data from a specified {@link File} source and stores it in the data storage.
   *
   * @param dataStorage the storage to write to
   * @param source      source file to read from
   * @throws IOException if there is an error reading the data
   */
  void readDataFromFile(File source, DataStorage dataStorage) throws IOException;
}
