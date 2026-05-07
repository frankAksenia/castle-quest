# Castle Quest

A turn-based AI-vs-AI treasure hunting game implemented with a separate Java client and server architecture.  
Two autonomous clients compete on a shared, procedurally generated map. Each AI must find its own treasure, pick it up, discover the opponent's castle, and reach it first to win the game.

## Project Overview

This project implements a competitive programming game in which two independently running AI clients play against each other on a shared map. The game is coordinated by a central server.

The only manual step is requesting or starting a new game. After that, all game actions are performed automatically by the client AI implementations.

Each client generates one half of the final map, registers with the server, exchanges map data through the server, and then starts exploring the complete game world. The goal is to locate the client's own treasure, collect it, find the opponent's castle, and reach it before the opponent does.

## Game Concept

Each game is played by two clients. Every client is controlled by an AI and acts without human input after startup.

The game flow is:

1. A new game is requested on the server.
2. Two clients register for the game.
3. Each client generates one valid random map half.
4. The server combines both map halves into one complete map.
5. Each AI selects its own starting position, which is also its castle.
6. The server places one treasure on each half of the map.
7. The AIs explore the map turn by turn.
8. Each AI must:
   - find its own treasure,
   - move to the treasure and collect it,
   - find the opponent's castle,
   - move to the opponent's castle with the treasure.
9. The first AI to reach the opponent's castle with its treasure wins.

## Architecture

The project follows a classic client/server architecture.

```text
+----------------+          HTTP/XML          +----------------+
|                | <------------------------> |                |
|   Client AI 1  |                            |                |
|                |                            |                |
+----------------+                            |                |
                                              |     Server     |
+----------------+          HTTP/XML          |                |
|                | <------------------------> |                |
|   Client AI 2  |                            |                |
|                |                            |                |
+----------------+                            +----------------+
```

The implementation is split into two separate applications:

- **Server:** Coordinates games, validates rules, stores game state, handles turns, and determines win/loss conditions.
- **Client:** Generates map halves, communicates with the server, makes AI decisions, sends actions, and displays the game state through a CLI.

## Tech Stack

- **Language:** Java 17
- **Build Tool:** Gradle
- **Framework:** Spring Boot
- **Web/API:** Spring Web MVC, Spring WebFlux
- **Database/Persistence:** Spring Data JPA, Hibernate ORM, SQLite
- **Serialization:** JAXB / XML marshalling
- **Validation:** Java Validation API, Hibernate Validator
- **Testing:** JUnit 5, Mockito, Hamcrest
- **Logging:** SLF4J, Logback
- **External Library:** Course-provided SE1/SWE Network Messages library
- **IDE Support:** Eclipse

## Core Gameplay Rules

- The game is played by exactly two clients.
- Each client is controlled by an AI.
- Human interaction is only required to start or request a game.
- The game is turn-based.
- A client may only act when it is its turn.
- Each turn consists of exactly one action.
- Clients are not allowed to skip their turn.
- Each AI has a maximum of 5 seconds to submit an action.
- A full game may not exceed 320 game actions.
- Invalid actions may cause the responsible client to lose immediately.
- The server is the authoritative source for all game state and rule validation.

## Client Responsibilities

The client is responsible for:

- registering for a game,
- generating a valid random map half,
- sending the map half to the server,
- retrieving the full game state from the server,
- selecting the starting position / own castle,
- deciding movement actions autonomously,
- finding the own treasure,
- collecting the treasure,
- finding the opponent's castle,
- reaching the opponent's castle with the treasure,
- displaying the game state in the command-line interface,
- stopping automatically after the game ends.

The client must not send actions after the game has ended.

## Server Responsibilities

The server is responsible for:

- creating and managing games,
- registering clients,
- receiving and validating map halves,
- combining map halves into a complete map,
- placing treasures,
- coordinating turn order,
- validating submitted actions,
- updating and storing game state,
- hiding and revealing game information,
- enforcing time and action limits,
- detecting rule violations,
- determining winners and losers,
- providing game state data to clients.

The server acts as the referee and is the authoritative system for all gameplay decisions.

## Map Generation

Each client generates one random map half with a size of **5 x 10 fields**.

The server combines both halves either:

- along the short side, resulting in a **5 x 20** map, or
- along the long side, resulting in a **10 x 10** map.

The client cannot control how the server combines the two halves.

### Terrain Types

Each field has exactly one terrain type:

- **Grass**
- **Mountain**
- **Water**

### Map Half Requirements

Each generated map half must satisfy the following conditions:

- At least 5 mountain fields.
- At least 24 grass fields.
- At least 7 water fields.
- Exactly one castle.
- The castle must be placed on a grass field.
- All non-water fields must be reachable from each other.
- No grass or mountain area may be isolated by water and/or map borders.
- Each border may contain less than half water fields:
  - maximum 4 water fields on long borders,
  - maximum 2 water fields on short borders.
- The map must be generated algorithmically and must not be hard-coded.

If a client submits an invalid map half, that client loses automatically.

## Movement Rules

The map is grid-based. A player figure can move only horizontally or vertically to directly adjacent fields.

Diagonal movement is not allowed.

### Terrain Movement Costs

| Terrain  |  Enter Cost |  Leave Cost | Notes                                                                  |
| -------- | ----------: | ----------: | ---------------------------------------------------------------------- |
| Grass    |    1 action |    1 action | Reveals hidden objects on the field when entered.                      |
| Mountain |   2 actions |   2 actions | Reveals hidden objects within one field distance, including diagonals. |
| Water    | Not allowed | Not allowed | Entering water causes an immediate loss.                               |

A movement command must be repeated for the required number of actions before the move is completed. Until enough movement commands have been sent, the visible position remains unchanged.

Changing direction before completing a movement resets the movement progress. Already consumed turns are not refunded.

## Hidden Information

At the beginning of the game, the following information is visible to each client:

- all terrain fields,
- all player positions,
- the client's own castle,
- the complete combined map structure.

The following information is hidden and must be discovered:

- the client's own treasure,
- the opponent's castle.

The opponent's player position is randomized during the first 16 rounds. After that, the real opponent position is reported.

## Winning and Losing Conditions

A client wins when:

1. it finds and collects its own treasure,
2. it discovers the opponent's castle,
3. it reaches the opponent's castle while carrying the treasure.

A client loses if it:

- submits an invalid map half,
- moves into water,
- moves outside the map,
- sends an action when it is not its turn,
- violates game rules,
- exceeds the maximum turn time,
- fails due to another server-validated rule violation.

If one client loses, the other client wins automatically.

### Prerequisites

Make sure the following tools are installed:

- Java 17
- Gradle
- Eclipse or another Java IDE
- SQLite

## Logging

Logging is implemented with SLF4J and Logback.

Useful logging areas include:

- client registration,
- map generation,
- map validation,
- game-state updates,
- movement decisions,
- hidden-object discovery,
- rule violations,
- win/loss events,
- server communication.

Logging configuration can be adjusted in the relevant `logback.xml`, `application.properties`, or `application.yml` file.

## Development Notes

- The server should always be treated as the authoritative source of truth.
- The client should not assume that locally calculated state is always correct.
- Every server response should be validated before being used by the AI.
- Invalid or unexpected server responses should be handled defensively.
- The AI should avoid illegal moves, especially movement into water or outside the map.
- Since movement over mountains is expensive, pathfinding should account for terrain movement costs.
- The randomized opponent position during the first 16 rounds should not be used to infer the opponent castle location.
- The CLI output is intended to help human observers understand the AI's behavior during the game.

## Possible AI Strategy Improvements

Possible areas for improving the client AI include:

- weighted pathfinding using Dijkstra or A\*,
- prioritizing mountain fields for wider discovery range,
- minimizing unnecessary direction changes,
- avoiding expensive terrain unless strategically useful,
- separating exploration strategy from target-reaching strategy,
- caching known discovered information,
- evaluating shortest valid paths based on terrain costs,
- switching objectives after treasure collection.

## License

This project was developed as part of a software engineering course assignment at the University of Vienna.  
Use, distribution, and modification may be subject to the rules of the course or institution.
