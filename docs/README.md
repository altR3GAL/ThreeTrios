# ***`ThreeTrios`***

[Overview](#overview) | [Features](#features) | [Installation](#installation) | [Usage](#usage) | [Project Structure](#project-structure) | [AI Strategies](#ai-strategies) | [Configuration](#configuration) | [Contributing](#contribution-guidelines) | [License](#license)

> [!IMPORTANT]
> ThreeTrios is a strategy-based card game implemented in Java. It enforces game rules while maintaining a structured game state.

## Overview

ThreeTrios is a Java-based card game that simulates strategic battles using a deck of unique cards. Players compete by placing cards on a grid, where battles determine the outcome based on card values and positions.

## Features

- **Turn-Based Gameplay**: Players take turns placing cards strategically.
- **AI Opponents**: Various AI strategies, including MiniMax and heuristic-based decisions.
- **Configuration Files**: Customizable game settings through external `.config` files.
- **Command Line Execution**: Run and configure games via terminal commands.
- **Graphical & Text-Based Views**: Supports both GUI and CLI-based game interfaces.
- **Multiple Strategies**: Different computer player strategies provide varied challenges.
- **Extensive Logging**: Debug and analyze gameplay with built-in logging features.

## Installation

### Prerequisites

- **Java 11 or later**
- **JUnit 4 for testing**

### Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/altR3GAL/ThreeTrios.git
   ```
2. Navigate to the project directory:
   ```sh
   cd ThreeTrios
   ```
3. Compile the project:
   ```sh
   javac -d bin src/cs3500/threetrios/**/*.java
   ```
4. Run the game:
   ```sh
   java -cp bin cs3500.threetrios.ThreeTrios
   ```

## Usage

### Basic Gameplay

1. Start the game using the command line.
2. Select player mode (Human vs. Human or Human vs. AI).
3. Place cards strategically on the grid.
4. Win by controlling the most cards when the grid is full.

### Example Command
```sh
java -cp bin cs3500.threetrios.ThreeTrios --config path/to/config.file
```

## Project Structure

```plaintext
ThreeTrios/
├── src/
│   ├── cs3500/threetrios/
│   │   ├── controller/ (Handles player input and game logic)
│   │   ├── model/ (Game mechanics and rules)
│   │   ├── view/ (Graphical and text-based UI components)
├── docs/
│   ├── config/ (Predefined game configurations)
│   ├── README.txt (Legacy project documentation)
├── test/
│   ├── cs3500/threetrios/ (JUnit tests for core functionality)
├── Assignment7Saturday.jar (Compiled game binary)
```

## AI Strategies

ThreeTrios features multiple AI-controlled opponents with different strategies:

- **MiniMax AI**: Uses a minimax algorithm to evaluate the best possible move.
- **Most Flips AI**: Prioritizes moves that result in the most card flips.
- **Least Flips AI**: Plays defensively by minimizing card flips.
- **Corner Strategy AI**: Focuses on occupying strong corner positions on the grid.

## Configuration

The game allows custom configurations through `.config` files located in the `docs/config` directory. These files define:

- Board size and layout
- Initial card distributions
- AI behavior parameters

To use a custom configuration, specify the file path when launching the game:
```sh
java -cp bin cs3500.threetrios.ThreeTrios --config docs/config/BoardFile_NoHoles.config
```

## Contribution Guidelines

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Open a **Pull Request**.

## License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.

