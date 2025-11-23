# Multiplayer Jeopardy Game

## Overview
This is a Java Maven project for a multiplayer Jeopardy game. The project is currently in its initial state with a basic "Hello World" application structure.

## Project Information
- **Language**: Java 19
- **Build Tool**: Maven 3.8.6
- **Group ID**: com.oopopps
- **Artifact ID**: multiplayerjeopardygame
- **Version**: 1

## Project Structure
```
OOP2 Project/
└── multiplayerjeopardygame/
    ├── src/
    │   ├── main/
    │   │   └── java/
    │   │       └── com/
    │   │           └── oopopps/
    │   │               └── App.java
    │   └── test/
    │       └── java/
    │           └── com/
    │               └── oopopps/
    │                   └── AppTest.java
    ├── target/
    └── pom.xml
```

## Recent Changes (November 23, 2025)
- Imported GitHub project into Replit environment
- Configured Java 19 compatibility (updated from Java 21)
- Set up Maven build system
- Created workflow to run the application
- Added .gitignore for Java projects

## How to Run
The application runs automatically via the configured workflow. To run manually:
```bash
cd "OOP2 Project/multiplayerjeopardygame"
mvn compile exec:java -Dexec.mainClass="com.oopopps.App"
```

## Current State
The application currently prints "Hello World!" to the console. The multiplayer Jeopardy game functionality has not yet been implemented.

## Dependencies
- JUnit 4.11 (test scope)
