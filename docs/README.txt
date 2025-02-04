#changes for part 3:
 - tests:
   - made all fields that were not private and could be private private
   - added tests for model interface methods - getGameState(), getPlayer(), canBePlayed(),
     canBeFlipped(), getScore(), getOpponent(), getCurrentPlayer(), calculateScore()
   - added tests for interface methods for the requested observations - gridSize,
     cellContents, getHand, getCellOwner

 - code:
   - added documentation for all methods, classes, and constructors that were missing them.
   - changed:
     - ThreeTriosTextView moved to view package and takes in readonly instead of full model
   - added interface methods for the requested observations - gridSize, cellContents, getHand, getCellOwner
   - added listening functionality to the view and model

EXTRA CREDIT:
extra credit strategies implemented:
  - can find all of the in src.cs3500.threetrios.model
    - LeastFlipsComputer
    - MiniMaxComputer
    - MergerComputer
tests for extra credit strategies:
  - can find all in test.cs3500.threetrios.model
    - LeastFlipsComputerTest
    - MiniMaxComputerTest
    - MergerComputerTest

Changes for part 2:
    Added functionality/methods:
    explain what functionality was missing, and why, and how you chose to add it.


    - canBePlayed: this function was missing because during homework 5 we did not need a method to check
    if a certain location could be played since our cell's could already do that. We chose to add it by
    adding a getter method of sorts.
    - getPlayer: this function was missing because during homework 5 we did not need a method to get the
    player at a certain location because our card class could already do something very similar to that.
    We chose to add it by adding a getter method of sorts.
    - canBeFlipped: this function was missing because during homework 5 we did not need a method to get the
    amount of cards a location can flip at the moment. We chose to add it through a recursive approach, similar
    but different to how we resolve battles.
    - getScore: this was missing because during homework 5 we did not need a method to get the score of a
    specific player since we had a similar method calculateScore that did essentially that but decided a
    winner at the end. We decided to implement it in a similar way to calculateScore but only have it return
    the score of the desired player.
    - getOpponent: this was missing because during homework 5 we did not need a method to get the opponent.
    we decided to implement it by using a getter method of sorts.
    - getPlayer: this was missing because during homework 5 we did not need a method to get the current player
    because we had a value currentTurn that essentially did the same thing. We decided to implement it by
    using a getter method of sorts.
    - getRedHand: this was missing because during homework 5 we did not need a method to get the red players
    hand since we already had a similar method getHand in the playerstructure that did the same thing. We
    decided to implement it by using a getter method of sorts
    - getBlueHand: this was missing because during homework 5 we did not need a method to get the blue players
    hand since we already had a similar method getHand in the playerstructure that did the same thing. We
    decided to implement it by using a getter method of sorts
    - Updated the viewing methods to allow the view to "read" the model.
        - Refactored into the ReadOnlyModel

    Changed methods and structures:
    - moved PlayerStructure out of ThreeTriosModel and into it's own class since having an inner class
      made mocking impossible. The inner class would conflict when a computer/strategy would try to access
      it.

Model Invarient:

    A class invariant for ThreeTriosModel would be: gameStarted is only true if startGame has run.
   1. This is a statement that can be answered with true or false.
   2. This statement can be satisfied at all points in time.
      When startGame has not run gameStarted
      is false since the constructor sets it to false, meaning that
      any time before startGame is
      called, gameStarted will be false. After startGame is called
      there are no other methods that
      can alter the state of gameStarted. In addition, startGame
      cannot be called again while gameStarted is true,
      meaning that gameStarted can never be mutated again and therefore
      satisfy the invariant statement.
   3. The constructor sets gameStarted to false, meaning that any time
      before startGame is called,
      gameStarted will be false, thus satisfying the invariant statement.
   4. There are no methods other than the constructor and startGame that
      modify gameStarted, the
      only other times gameStarted is used it only being viewed, and thus
      cannot be changed.
      Thus, there are no methods that invalidate the class invariant.

 Overview:
 What Problem Is The Codebase Trying To Solve:
 The primary problem that the codebase is trying to solve is how do we enforce the rules of the game while maintaining
 a proper game state and proper player interactions with the game. In addition to that, the codebase is trying to
 ensure that the game can handle any and all exceptions without crashing the game.

 What High-Level Assumptions Are Made In The Codebase:
 Users who use the codebase will need Java 11 and access to JUNIT4 in order to run the codebase and tests.
 Data preservation is not handled in this codebase, users will have to create their own files in order to store game
 states and run them again for future use.
 Users can come into the codebase and use the interfaces to modify and adapt the code for their specific purposes.
 Config files can be modified so that basic settings can be changed.
 Users have an understanding of the game and the rules before using the codebase.
 Users have a moderate-high understanding of Java and can read code.


 Quick Start:
  - Clone the repository
  - No environment variables to set up
  - If you want to check out the functionality of the classes thru tests then one should run these recommended tests.
    - Recommended Tests: BattleTest, ThreeTriosModelTest, ThreeTriosTextViewTest
  - If you want to check out the functionality of the strategies thru tests then one should run these recommended tests.
      - Recommended Tests: MostFlipsComputerTest, CornerComputerTest
  - Quick Start Code:

    ThreeTriosModel game = new ThreeTriosModel(5, 5, fiveByFiveGameCellList);
    game.startGame();
    CardImpl card = new CardImpl(Color.RED, Value.ONE, Value.SEVEN, Value.FOUR, Value.EIGHT);
    game.getCurrentPlayer().drawCard(card);
    game.playCard(card, 0, 0);

    ^ this code is for a simple 5x5 model where the user plays one Red card.


  Key Components:
  ThreeTriosModel:
  - Driving Components:
    - startGame: This method is the central driving component of the model as it sets the state of the game and
    all components adjacent such as players, hands, cells. The most central component of the method is that it sets
    the state of the game to started, allowing all other methods to work.
    - playCard: this method is a driving component as playing cards to the board is a super important part of the
    game that triggers many other functions/components of the model such as battles.
    - switchTurn: This method is a driving component as is changes the players' turn, switching the current player
    with the "inactive" player. This is a driving component since changing the turn of a player influences heavily
    what players can and cant do.
    - resolveBattles: This method is a driving component since it causes all of the battles in the BattleStack to
    be fought, which in turn can cause cards to flip colors and for more battles to be created.
    - quitGame: This method is another central driving component as it ends the game when the player chooses to quit.
    - calculateScore: This method is a driving component since it determines the winner of the game by counting the
    cells and the amount of colors of each card to find the winner.
  - Driven Components:
    - battleStack: the battleStack is a driven component as it contains a stack of battles that change as playCard
    and resolveBattle are called.
    - deck: the deck is a driven component as it has elements removed when startGame/dealCards is called.
    - grid: the grid is a driven component as it is the representation of the board that is ever changing as long as
    the game has not been won or quit.
    - getCurrentPlayer is a driven component that is driven by switchTurn/playCard which call it.
    - triggerBattles is a driven component that is driven by playCard, resolveBattles, it creates/sets up battles for
    cells that are next to it. It also adds battles to battleStack if necessary.

  ThreeTriosTextView:
  - Driving Components:
    - render: This method is the central driving component as it is the method that is called to activate the rest of
    the processes to render the game state.
    - createOutput: this method is the secondary driving component, it calls all of the helper/driven methods that
    output different parts of the view of the the game.
  - Driven Components:
    - renderBoard: the method is a driven method that is called by createOutput, it creates a string of the game board
    at the state that render was called.
    - renderHand: the method is a driven method that is called by createOutput, it creates a string of the current
    player's hand at the state that render was called.

  ComputerPlayers (interface for all strategies):
  - Driving Components:
    - selectBestMove: This method is the central driving component as it is the method that is called (almost)
    everytime a computer player (strategy) is used, it also calls the other component calculateMoveScore,
    driving it to do it's functionality.
    - getStrategyName: This method is a secondary driving component, it is independent and is only called
    when the user wants to know the strategy the current computer is using.
  - Driven Components:
    - calculateMoveScore: This method is a driven method that is called by selectBestMove, it calculates
    the score for a cell at a given point, score being how good the cell is in the context of the strategy.

  ThreeTriosView:

  - Driving Components:
    - ThreeTriosPanel
    These panels are able to draw cards in cells. The cell dimensions are actively re-calculated based on changes
    to the size of the grid, hand, and the frame (through resizing)
        - HandPanel
        These panels store the visual data for the hands. All hand panels are stored separately
        so the card height is unique to that hand's number of cards.
        - GridPanel
        These panels store the visual data for the Cell[][] grid used by the model. The size of the cells
        changes based on the ratios of the frame. By default, the grid panel sets visually pleasing dimensions
        for its cells.
    - ThreeTriosView
    This is the frame for the GUI, the frame stores two hand panels and one grid panel. The frame is resizable.

  ThreeTriosPlayerController:
  - Driving Components:
    - playerTurnHeard: This method is the central driving component for the controller since all other functions are
      not able to be acted upon until this method confirms that it is the player's turn.
  - Driven Components:
    - playerEndOfTurnHeard: This method is a driven component that helps the rest of the game/model navigate
      what it needs to do after a player's turn has "ended".
    - gameEndHeard: This method is a driven component that helps the player/game navigate what happens
      when the game ends/is quit.
    - positionChosenHeard: This method is a driven component that helps the player play the card and navigate
      the things to do after a card is attempted to be played.
    - cardChosenHeard: This method is a driven component that helps the model act upon a player selecting a card.

  ThreeTriosComputerController:
    - Driving Components:
      - playerTurnHeard: This method is the central driving component for the controller since the computer
        immediately acts, placing card given what the strategy it's uses deems as best for it, it doesn't rely
        on other methods or things to make decisions, it is essentially the only thing that is run in the
        controller when the game is not quit or has ended.
    - Driven Components:
      - playerEndOfTurnHeard: This method is a driven component that helps the rest of the game/model navigate
        what it needs to do after a player's turn has "ended".
      - gameEndHeard: This method is a driven component that helps the player/game navigate what happens
        when the game ends/is quit.




 Key Subcomponents:
 CardImpl:
  - Color: The enum that represents the color of a card. It exists because each card needs to hold information
  about it's typing and "role" in the bigger context of many cards on a board.
  - Value: The enum that represents the directional value of each numerical element on the card. It exists so
  that cards can be compared between each other.
  - Card Name: String value that gives each card a "unique" name. The name makes sure that each card is "distinct"
  so that even if they are numerically and color same they can be distinguished.

 Cell:
  - Row and Col: Integer representation of the Cell's location on a game board. The Cell needs to know where it is
  since this is the way that we are tracking the location of placed cards and available non-hole cells.
  - isHole: Boolean representation of the Cell's typing. If a cell is a hole that means that we do not need to
  check for things like if it has been played to or not since it cannot be played to at all if it is a hole.
  - Card: CardImpl representation of the card that is placed in the cell. A cell has the ability to contain a card,
  knowing the specific CardImpl will help this cell interact with other cells. If the specific cell is acted
  upon and it contains the CardImpl, then it will be simple to perform operations on it.
  - CellPlayed: boolean value to track whether a non-hole cell has been played in. The usage of this is to prevent
  players from placing one too many cards into a cell.

 Deck:
  - cards: List of CardImpls that represent the deck (a collection of cards). This is used so that if the user wants
  to have their own custom deck it is easy to assign it to them. Having a list makes it really easy to work with in
  the deck since the list maintains the structure of the deck very easily.
  - deckSize: Integer representation of the deck's size. It is also used to help determine the size of the hand when
  deck needs to randomly generate cards to put into the deck.

 Battle:
  - Cell: Representation of position in the game that has a card in it (there will always be a card in cells in battle
  since battles with empty cells are not added to battle stack). The cell is used to ensure that battles are handled
  correctly in that the right values are battled and the correct changes to a cell's card happen after a battle is won
  lost or tied.
  - connection: Representation of the direction that the battle is going in. It is used to ensure that battles are
  conducted correctly, since the enum was made so that the attacking side is always the left value, battles can be
  ensured (that the correct "fight" happens). This prevents the attacker from being flipped if it loses.

 HumanPlayer:
  - hand: List of CardImpls that represent the cards currently owned by the player in their hand. It is used so that
  each player has their own individual set of cards that can be managed and assigned to them.
  - playerstructure: the structural representation of the player. This is used for identification so that the player,
  the player's hand, and all other things belonging to the player can be identified as theirs as well as all functions.

 MachinePlayer:
   - computer: The computer/strategy that the machine player will use to make all of it's decisions, including
     selecting the best card, and the best cell.



  Resizable Listeners:
   - MaximizeListener:
    - This listener uses a resizablePanelListener to resize the panels whenever the frame is maximized.
   - ResizablePanelListener:
    - This listener resizes the panels whenever the frames is resized (in any way).

 Source Organization:
 In the main source folder there is the main cs3500.threetrios directory and the cs3500.threetrios.ConfigurationFiles
 directory. The main cs3500.threetrios directory serves to contain all of the classes and interfaces that are related
 to the main model that we designed. The cs3500.threetrios.ConfigurationFiles directory contains all of the files
 that are needed to run the ConfigurationFileReader.
 In the test source folder there is only the main cs3500.threetrios directory. This directory serves to contain all
 of the tests for all of the classes (excluding the AI player).

Either explain for each component where to find it in your codebase:
 - docs:
   - contains all of the configuration files for setting up game state.
   - contains the README
   - docs.mockconfigs:
     - contains all of the configuration files for setting up the game state for testing the strategies

 - src/cs3500.threetrios.controller:
   - contains all of the interfaces and classes that work with the controller.
     - Interfaces:
       - ModelListener: interface for model listener.
       - ModelStatus: interface for model status checker.
       - Player: interface for players (both computer and human).
       - PlayerAction: interface for player action listener.
       - PlayerListener: interface for player listener.
     - Classes:
       - AbstractPlayer: abstract class for all player functions.
       - HumanPlayer: class for human players.
       - MachinePlayer: class for machine players.
       - ThreeTriosComputerController: class for computer controller.
       - ThreeTriosPlayerController: class for player controller.
       - ThreeTriosPlayerControllerMock: Mock for player controller testing.

 - src/cs3500.threetrios.model:
   - contains all of the interfaces, classes, and enum classes that work with the model.
   - Interfaces:
     - Card: interface for cards
     - Player: interface for players
     - TripleTriadModel: interface for game model
     - ReadOnlyTripleTriadModel: interface for read only functions in game model.
     - ComputerPlayers: interface for computer strategies.
   - Classes:
     - Battle: class for all of the battle instances
     - CardImpl: class for all of the Cards
     - Cell: class for all of the cells
     - ConfigurationFileReader: class for reading config files
     - Deck: class for deck handling
     - ThreeTriosModel: class for the main game model
     - ThreeTriosViewModel: class for the view of the model.
     - ComputerGeneralFunctions: class for the functions used in all of the computers.
     - CornerComputer: class for the computer using the corner strategy
     - LeastFlipsComputer: class for the computer using the least flips (defensive) strategy
     - MergerComputer: class for the computer that merges strategies
     - MiniMaxComputer: class for the computer using the mini max strategy
     - MockThreeTriosModel: class for the mock of the ThreeTriosModel
     - MostFlipsComputer: class for the computer using the most flips (attacking) strategy
     - PlayerStructure: class for the player that only has the functionality needed for the model to run
     - ThreeTrios: placeholder class for the main function.
     - Enums:
          - Color: the color of each card
          - Value: the value of each side of the card
          - Connection: the direction in which the battle is going.
 - src/cs3500.threetrios.view:
   - contains all the interfaces and classes that are used to display the game
   - Interfaces:
     - TripleTriadFrame: interface for all frames used by the view.
     - TripleTriadGridPanel: interface for all grid panels used by the view.
     - TripleTriadHandPanel: interface for all hand panels used by the view.

   - Classes:
     - ThreeTriosView: Extension of JFrame, handles the view of the model.
     - MaximizeListener: Listener for frame maximization.
     - ResizablePanelListener: Listener for all kinds of frame resizing.
     - ThreeTriosTextView: class for the text output/view of the model.
     - ThreeTriosGridPanel: Extension of ThreeTriosPanel, handles the grid panel rendering and clicking.
     - ThreeTriosGridPanel: Extension of ThreeTriosPanel, handles the hand panel rendering and clicking.
     - ThreeTriosPanel: Abstract Class, Extension of JPanel, handles the capabilities of all panels.

 - test/cs3500.threetrios.model
   - contains all of the tests for the classes
     - BattleTest: tests if the battle class and all it's methods work
     - CardImplTest: tests if the cardImpl class and all it's methods work
     - CellTest: tests if the cell class and all it's methods work
     - DeckTest: tests if the deck class and all it's methods work
     - ThreeTriosModelTest: tests if the threeTriosModel class and all it's methods work
     - ThreeTriosTextViewTest: tests if the threeTriosTextView class and all it's methods work
     - CornerComputerTest: tests if the corner strategy and all it's methods work
     - LeastFlipsComputerTest: tests if the least flips (defensive) strategy and all it's methods work
     - MergerComputerTest: tests if the merger (combiner) strategy and all it's methods work
     - MiniMaxComputerTest: tests if the mini max strategy and all it's methods work
     - MostFlipsComputerTest: tests if the most flips (attacking) strategy and all it's methods work
 - test/cs3500.threetrios.controller
   - contains all of the tests for the controllers and players.
     - ComputerControllerTest: tests if the computer controller and all its important functions work.
     - HumanPlayerTest: tests if the HumanPlayer class and all it's methods work.
     - HumanPlayerTest: tests if the MachinePLayer class and all it's methods work.
     - PlayerControllerTest: tests if the player controller and all it's important functions work.
