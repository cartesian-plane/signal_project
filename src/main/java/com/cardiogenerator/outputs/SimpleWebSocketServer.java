package com.cardiogenerator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class SimpleWebSocketServer extends WebSocketServer {

  public SimpleWebSocketServer(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
    System.out.println("New connection: " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    // Not used in this context
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    // not much to do in the case of message corruption,
    // as the protocol takes care of that by itself
    // https://datatracker.ietf.org/doc/html/rfc6455#section-5
    ex.printStackTrace();
  }

  @Override
  public void onStart() {
    System.out.println("Server started successfully");
  }
}
