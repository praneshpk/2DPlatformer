# Multi-threaded Server

Beginning stages of creating a game engine using Processing and multi-threading

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

This project requires Maven and Java 8 is installed.

### Easy Mode

Run the `LaunchGame.sh` shell script in the root of the project to compile and run the server and client processes. The script will run one client by default.*
```
./LaunchGame.sh [# of clients]
```

## Compiling

Run the following command to compile the program
```
mvn package
```

## Running the game server process

Run the following command to start the game server
```
java -cp target/multi-threaded-server-0.2.0.jar core.Server
```

## Running the client process

Run the following command to connect to the server and start the Processing sketch.*
```
java -cp target/multi-threaded-server-0.2.0.jar core.Main
```

Use the left and right arrow keys to move around, spacebar to jump.


**A maximum of 6 users can be connected to the server at once, as determined in the core.network.Server class*
