# Poker Game - Strategy Competition

A Texas Hold'em Poker engine that allows players to implement custom strategies and compete against each other. Perfect for strategy competitions and testing AI/algorithmic approaches to poker gameplay.

---

## System Requirements

- **Java Version**: Java 17 or higher
- **Build Tool**: Maven 3.6+
- **IDE**: Visual Studio Code (recommended for prettier log display)

---

## Getting Started

### 1. Setup & Installation

```bash
# Clone or navigate to the project directory
cd poker-competition

# Clean build the project
mvn clean install

# Compile the project
mvn compile
```

### 2. Running the Game

To run a game with the default configuration:

```bash
mvn exec:java
```

This command will:
- Compile all source files
- Execute the `MainApplication` class
- Run the poker game with the configured players and strategies

---

## Game Rules (Custom Implementation)

This is **not** standard Texas Hold'em poker. We've implemented custom rules optimized for strategy competition:

### Fee System
- **Mandatory Fee**: Each player pays a **$5 fee** at the start of each round
- **No Blinds**: Unlike traditional poker, there are no small blinds or big blinds
- **First Player Rotation**: The first player to act rotates clockwise after each game

### Community Cards Reveal
- **Round 1**: **3 community cards** are revealed 
- **Round 2**: 1 additional card revealed (4 total)
- **Round 3**: Final card revealed (5 total)
- Players have 2 hole cards + 5 community cards for hand evaluation

---

## Implementing Your Own Strategy

### Step 1: Create Your Strategy Folder

Create a new folder under `src/main/java/com/mudit/poker/Algos/` with your name:

```
Algos/
├── SampleStrategy.java
├── ManualPlayStrategy.java
├── YourName/                     # Create this folder
│   └── YourNameStrategy.java     # Your strategy class
└── ...
```

### Step 2: Implement the PlayerStrategy Interface

Create a class that implements `PlayerStrategy`:

```java
package com.mudit.poker.Algos.YourName;

public class YourNameStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        // YOUR STRATEGY LOGIC HERE
        // Return a PlayerMove with your decision
        return new PlayerMove(MoveType.CALL, 0);
    }

    @Override
    public void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        // Called when any player makes a move (including yourself)
        // Use this to update your strategy based on game events
    }

    @Override
    public void onSingleGameEnd(GameResult gameResult) {
        // Called when a single game ends
        // Used for post-game analysis (don't modify the result)
    }
}
```

---

## PlayerStrategy Interface Details

The `PlayerStrategy` interface has three core methods:

### 1. `makeYourMove()`

```java
PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards)
```

**Called**: When it's your turn to make a move

**Parameters**:
- `GameStateInfo gameState`: Contains game metadata
  - `getRound()`: Current round (1-3)
  - `getTotalPot()`: Current pot size
  - `getHighestCurrentBid()`: Highest bid this round
  - `getRevealedCommunityCards()`: Community cards revealed so far

- `GamePlayerStatusInfo myStatus`: Your current game status
  - `getPlayerId()`: Your player ID
  - `getCurrentAmount()`: Your remaining money
  - `getCurrentRoundBid()`: How much you've bet this round
  - `getLastMoveTypeInCurrentRound()`: Your last move (FOLD, CALL, RAISE, CHECK)
  - `getBidsPerRound()`: Your bids in each round (List<Integer>)

- `List<Card> myCards`: Your two hole cards

**Returns**: `PlayerMove` with your decision
- `MoveType.FOLD`: Exit the current game
- `MoveType.CALL`: Match the highest bid
- `MoveType.RAISE`: Increase the bet (specify amount)
- `MoveType.CHECK`: Pass without betting (if no one has bet)

### 2. `onNewMove()`

```java
void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus, List<Card> myCards)
```

**Called**: After any player (including yourself) makes a move

**Use Case**: Update your strategy based on opponent behavior, track patterns, etc.

### 3. `onSingleGameEnd()`

```java
void onSingleGameEnd(GameResult gameResult)
```

**Called**: After a single game ends

**Use Case**: Analyze results, calculate statistics (don't modify the result)

---

## Running the Game

### Edit MainApplication.java

Open `src/main/java/com/mudit/poker/GamePlay/MainApplication.java`:

```java
public class MainApplication {
    public static void main(String[] args) throws Exception {
        int INITIAL_MONEY = 1000;           // Starting money for each player
        int NUM_GAME_PLAYS = 1;              // Number of games to play
        
        List<Player> players = Arrays.asList(
            new Player(1, new YourNameStrategy()),
            new Player(2, new OtherPlayerStrategy()),
            new Player(3, new YourSecondStrategy())
            // Add more players as needed
        );

        PokerGamePlay game = new PokerGamePlay(INITIAL_MONEY, players, NUM_GAME_PLAYS);
        game.run();
    }
}
```

**Customizable Parameters**:
- `INITIAL_MONEY`: Starting amount for each player (default: 1000)
- `NUM_GAME_PLAYS`: Number of games to run in the tournament (default: 1)
- `players`: Add/remove players and assign different strategies

### Example Configuration

```java
List<Player> players = Arrays.asList(
    new Player(1, new SampleStrategy()),
    new Player(2, new YourNameStrategy()),
    new Player(3, new AnotherStrategy())
);
int INITIAL_MONEY = 5000;
int NUM_GAME_PLAYS = 10;
```

Then run:
```bash
mvn exec:java
```

---

## Manual Play Mode

Want to play against your strategies manually? Use `ManualPlayStrategy`!

### In MainApplication.java:

```java
List<Player> players = Arrays.asList(
    new Player(1, new ManualPlayStrategy()),  // You play manually
    new Player(2, new YourNameStrategy())     // Your strategy
);
```

### During Gameplay:

The game will display in your terminal:
- 🎮 Game status (current round, pot, highest bid)
- 🎴 Community cards revealed
- 🎴 Your hole cards
- 👤 Your status (money, current bid, last move)
- 💰 Your bids per round

### Making Moves:

When prompted, enter:
- `FOLD` - Exit the game
- `CALL` - Match the current bid
- `RAISE` - Increase the bet (you'll be prompted for amount)
- `CHECK` - Pass without betting (if allowed)

---

## Viewing Logs in Pretty Format

### ⚠️ Important Note on Terminal Output

For the best viewing experience with emojis and Unicode box characters:

**Use the VS Code Terminal**: Open VS Code's integrated terminal (`Ctrl+`` or View → Terminal)

The default Windows Command Prompt may not display emojis and Unicode characters correctly. VS Code's terminal has proper UTF-8 support and will render:
- All emojis (🎮🎴🏆♥️♦️♣️♠️)
- Box drawing characters (╔═╗║└┘├┤)
- Color formatting

### Running in VS Code:

1. Open the project in VS Code
2. Open integrated terminal: `Ctrl+``
3. Run: `mvn exec:java -Dexec.mainClass="com.mudit.poker.GamePlay.MainApplication"`

---

## Testing

Run the test suite to ensure everything works:

```bash
mvn test
```

This will run all unit tests for the poker engine.

---

## Utility Classes for Strategy Development

### CombinationCalculator

The `CombinationCalculator` class provides utility methods to help you analyze hands and make informed decisions:

#### Key Methods:

**1. `getCardCombination(List<Card> cards)`**
- **Purpose**: Calculate the highest possible poker hand from exactly 7 cards
- **Input**: List of 7 cards (2 hole cards + 5 community cards)
- **Output**: `CombinationResult` containing the hand type and the 5 cards forming that hand
- **Use Case**: Evaluate your own hand strength or analyze opponents' potential hands

**2. `compareCombinations` Comparator**
- **Purpose**: Compare two `CombinationResult` objects to determine which hand is stronger
- **Input**: Two `CombinationResult` objects
- **Output**: Comparison result (follows standard Java comparator convention)
  - Negative if first is better
  - Positive if second is better
  - Zero if equal (tie)
- **Use Case**: Rank hands when evaluating multiple scenarios

```java
CombinationResult hand1 = CombinationCalculator.getCardCombination(cards1);
CombinationResult hand2 = CombinationCalculator.getCardCombination(cards2);

int comparison = CombinationCalculator.compareCombinations.compare(hand1, hand2);
if (comparison < 0) {
    System.out.println("Hand 1 is better!");
} else if (comparison > 0) {
    System.out.println("Hand 2 is better!");
} else {
    System.out.println("It's a tie!");
}
```

**3. Helper Methods**
- `checkFlush()` - Detect if cards contain a flush
- `checkStraight()` - Detect if cards contain a straight
- `getCardsListByDecreasingFrequency()` - Group cards by frequency (useful for pairs, three-of-a-kind, etc.)

---

## Troubleshooting

**Q: Emojis showing as "?" in terminal**
A: Use VS Code's integrated terminal instead of Command Prompt

**Q: Maven command not found**
A: Ensure Maven is installed and in your PATH. Check with `mvn --version`

**Q: Compilation errors**
A: Ensure Java 17+ is installed: `java --version`

**Q: Strategy class not found**
A: Check that your strategy package matches the folder structure and class imports are correct

---

## Contributing

To add your strategy to the competition:
1. Create a folder with your name under `Algos/`
2. Implement `PlayerStrategy` in your folder
3. Raise a pull reqeuet
4. Run tournaments and track your wins!

---

Enjoy building and competing! 🎮♠️♥️♦️♣️