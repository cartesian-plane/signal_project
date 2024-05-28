package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Sets up a WebSocket client, to retrieve the generated data.
 * <p>
 *   This client only contains logic for the retrieval of messages, leaving parsing & storage
 *   up to the {@link DataReader}.
 * </p>
 */
public class SimpleWebSocketClient extends WebSocketClient {

  // with a BlockingQueue of size 1000, the Reader will never miss a message
  // ~600 patients is where it starts to break down (no more stack space for threads)
  // given this upper bound, there's no need to optimise this further;
  // (the bottleneck is due to the patient threads themselves)
  public final BlockingDeque<String> messages = new LinkedBlockingDeque<>(1000);

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
  public void onMessage(String message) {
    messages.add(message);
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("closed with exit code " + code + " additional info: " + reason);

  }

  @Override
  public void onError(Exception ex) {
    // not much to do in the case of message corruption,
    // as the protocol takes care of that by itself
    // https://datatracker.ietf.org/doc/html/rfc6455#section-5
    messages.clear();
    System.err.println("an error occurred:" + ex);

  }
}
