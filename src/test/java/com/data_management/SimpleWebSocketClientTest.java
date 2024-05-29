package com.data_management;

import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class SimpleWebSocketClientTest {

  private SimpleWebSocketClient client;
  private URI serverURI;

  @BeforeEach
  void setUp() throws URISyntaxException {
    serverURI = new URI("ws://localhost:8080");
    client = spy(new SimpleWebSocketClient(serverURI));
  }


  @Test
  @DisplayName("Receive Messages")
  void testOnMessage() {
    String expectedMessage = "{\"patientId\":15,\"measurementValue\":"
        + "4.641559693126796,\"label\":\"RedBloodCells\",\"timestamp\":1715519279864}";
    client.onMessage(expectedMessage);
    assertEquals(expectedMessage, client.messages.poll());
  }
}