# Multiplayer Jeopardy Game

## Overview
This is a fully functional Java Maven project for a multiplayer Jeopardy game. The game is a console-based application that supports 1-4 players and uses various design patterns (Command, Strategy, Factory, Observer).

## Project Information
- **Language**: Java 19 (GraalVM)
- **Build Tool**: Maven 3.8.6
- **Group ID**: com.oopopps
- **Artifact ID**: multiplayerjeopardygame
- **Version**: 1
- **Type**: Console Application (CLI)

## Project Structure
```
OOP2 Project/
└── multiplayerjeopardygame/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/
    │   │   │       └── oopopps/
    │   │   │           ├── command/        # Command Pattern
    │   │   │           │   ├── Command.java
    │   │   │           │   ├── AnswerCommand.java
    │   │   │           │   └── RemoteControl.java
    │   │   │           ├── display/        # Observer Pattern
    │   │   │           │   ├── ScoreObserver.java
    │   │   │           │   └── ScoreBoard.java
    │   │   │           ├── report/         # Strategy Pattern
    │   │   │           │   ├── ReportStrategy.java
    │   │   │           │   ├── TextReportStrategy.java
    │   │   │           │   └── PDFReportStrategy.java
    │   │   │           ├── App.java        # Entry Point
    │   │   │           ├── GameEngine.java # Core Game Logic
    │   │   │           ├── Player.java
    │   │   │           ├── Question.java
    │   │   │           ├── EventLogger.java
    │   │   │           ├── ReportGenerator.java
    │   │   │           ├── QuestionParser.java
    │   │   │           ├── ParserFactory.java  # Factory Pattern
    │   │   │           ├── XMLQuestionParser.java
    │   │   │           ├── JSONQuestionParser.java
    │   │   │           └── CSVQuestionParser.java
    │   │   └── resources/
    │   │       ├── sample_game_CSV.csv
    │   │       ├── sample_game_JSON.json
    │   │       └── sample_game_XML.xml
    │   └── test/
    │       └── java/
    │           └── com/
    │               └── oopopps/
    │                   └── AppTest.java
    ├── target/         # Maven build output
    └── pom.xml
```

## Recent Changes (November 26, 2025)
- Imported GitHub project into Replit environment
- Installed Java 19 (GraalVM) with Maven 3.8.6
- Verified all Maven dependencies (JUnit, JSON library, Apache PDFBox)
- Successfully compiled the project
- Created workflow to run the application
- Confirmed .gitignore is properly configured for Java projects
- Verified application runs successfully

## How to Run
The application runs automatically via the configured workflow "Run Jeopardy Game". 

To run manually:
```bash
cd "OOP2 Project/multiplayerjeopardygame"
mvn compile exec:java -Dexec.mainClass="com.oopopps.App"
```

## Game Features
- **Question Formats**: Supports XML, JSON, and CSV question files
- **Multiplayer**: 1-4 players supported
- **Turn-based Gameplay**: Players take turns answering questions
- **Scoring System**: Correct answers add points, wrong answers subtract points
- **Report Generation**: Automatic text and PDF reports after game ends
- **Event Logging**: All game events logged to CSV file

## Design Patterns Used
1. **Command Pattern**: `AnswerCommand`, `RemoteControl` - encapsulates answer actions with undo capability
2. **Strategy Pattern**: `ReportStrategy`, `TextReportStrategy`, `PDFReportStrategy` - flexible report generation
3. **Factory Pattern**: `ParserFactory` - creates appropriate parser based on file type
4. **Observer Pattern**: `ScoreBoard`, `ScoreObserver` - tracks and displays score updates

## Dependencies
- JUnit 4.11 (test scope)
- org.json 20240303 (JSON parsing)
- Apache PDFBox 2.0.30 (PDF report generation)
- Apache PDFBox FontBox 2.0.30 (font handling)

## Current State
The application is fully functional and ready to use. It runs as a console-based interactive game where players answer Jeopardy-style questions.
