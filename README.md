# Multi-threaded Server

Beginning stages of creating a game engine using Processing and multi-threading

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

This project requires you having Maven and Java installed.

### Compiling

Run the following command in the root of the project, assuming Maven is installed.
```
mvn package
```

### Section 1: Processing

Run the following command to view the implementation of this section
```
java -cp target/multi-threaded-server-0.1.0.jar Main
```

Use the left and right arrow keys to move around, spacebar to jump. Collision detection is printed in the console for now.

### Section 2 & 3: Multi-threaded Networking

The user will need to at least start up the Server class
```
java -cp target/multi-threaded-server-0.1.0.jar Server
```
and then up to three Client classes can be started simultaneously.
```
java -cp target/multi-threaded-server-0.1.0.jar Client
```

The server can be stopped by sending a SIGINT signal, while the client can enter ':q' to exit the server.