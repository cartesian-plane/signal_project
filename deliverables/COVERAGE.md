The unit tests do not cover:
* data format errors involving the WebSocketClient, 
as all the JSON serialization/deserialization is done with an external library.
* message integrity checks, as the WebSocket protocol takes care of that
([RFC 6455](http://tools.ietf.org/html/rfc6455), [RFC 7692](http://tools.ietf.org/html/rfc7692)).