package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class SimpleWebSocketClient extends WebSocketClient {


  public static void main(String[] args) throws URISyntaxException {
    WebSocketClient client = new SimpleWebSocketClient(new URI("ws://localhost:8080"));
    client.connect();
  }

  public SimpleWebSocketClient(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public SimpleWebSocketClient(URI serverURI) {
    super(serverURI);
  }

  @Override
  public void onOpen(ServerHandshake serverHandshake) {
    System.out.println("new connection opened (client)");
  }

  @Override
  public void onMessage(String s) {

  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("closed with exit code " + code + " additional info: " + reason);

  }

  @Override
  public void onError(Exception ex) {
    System.err.println("an error occurred:" + ex);

  }
}
