# 2D Platformer Engine

This is a completed 2D platformer game engine using Processing 3. The engine supports event management, record / playback, scripting, and multiple clients over the network using multi-threading. A few example scripts are provided to test the functionality of the engine, as well as a basic Space Invaders game created using the engine.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

This project requires Maven and Java 8 is installed.

## Easy Mode

### Space Invaders
Run the `LaunchGame.sh` shell script in the root of the project to compile and run Space Invaders.

### Scripting
Run the `LaunchScript.sh` shell script in the root of the project to compile and run the scripting example. This demonstrates scripting game object behaviors as well as event handling.

### Game Engine

Run the `LaunchEngine.sh` shell script in the root of the project to compile and run the server and client processes. The script will run one client by default.*
```
./LaunchEngine.sh [# of clients]
```

## Manual setup

### Compiling

Run the following command to compile the program
```
mvn package -Dmaven.test.skip=true
```

### Running the game engine server process

Run the following command to start the game server
```
java -cp target/2DPlatformerEngine-0.4.0.jar core.Server
```

### Running the game engine client process

Run the following command to connect to the server and start the Processing sketch.*
```
java -cp target/2DPlatformerEngine-0.4.0.jar core.Main
```

## Controls
### Space Invaders
|Key|Description|
|---|-----------|
| Left / Right Arrow keys | Move left / right  |
| Spacebar | Shoot |

### Game Engine
|Key|Description|
|---|-----------|
| Left / Right Arrow keys | Move left / right  |
| Spacebar | Jump |
| | Pause (in recording mode)|
| r | Toggle recording |
| q | Quit playback mode |
| 1 | Playback at normal speed |
| 2 | Playback at double speed |
| 3 | Playback at half speed |

The Sketch window will have a red shade to indicate that recording is in progress and will have a gray shade to indicate that the user is in playback mode.



**A maximum of 6 users can be connected to the server at once, as determined in the core.network.Server class*
