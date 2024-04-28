# Data Access Layer

The **Data Access Layer** handles different sources of patient data within the Cardiovascular Health Monitoring System which is then standardized, validated, and processed. Finally, the processed data is further processed and stored in the Data Storage System for real-time monitoring and analysis. There are three main classes:

## DataListener

DataListener ingests the data from various sources (see DataListener subclasses) into the Cardiovascular Health Monitoring System. It provides a unified interface for listening to incoming data streams and facilitates their processing within the system. Sublclasses of DataListener are used to handle different data sources, which makes the system more efficient in dealing with diverse input methods.

DataListener subclasses:

    FileDataListener reads data from files, manages file I/O operations, and listens for changes in the current file locations. 

    TCPDataListener establishes socket connections to receive data from remote devices or applications. It enables real-time data streaming and interaction with networked devices, supporting continuous monitoring and analysis.

    WebSocketDataListener establishes WebSocket connections to communicate with web clients or services. It allows for bidirectional communication and event-driven data transmission, updating dynamically.

## DataParser

 Standardizes raw data from the DataListeners into a uniform format and then sends it for further processing. It parses, validates, and transforms data, ensuring consistency and compatibility across different sources.

 ## DataSourceAdapter

Receives standardized data from the DataParser and performs final processing and storage. It interacts with the Data Storage System to persist processed data, maintaining integrity and accessibility.
