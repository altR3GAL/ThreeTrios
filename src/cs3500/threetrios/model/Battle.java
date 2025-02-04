package cs3500.threetrios.model;

/**
 * the class battle that holds all battle information and resolves battles.
 */
public class Battle {
  private final Cell attackingCell;
  private final Cell defendingCell;
  private final Connection connection;

  /**
   * constructor for Battle instances.

   * @param attackingCell the cell that initializes the battle.
   * @param defendingCell the cell that is being battled.
   */
  public Battle(Cell attackingCell, Cell defendingCell) {
    this.attackingCell = attackingCell;
    this.defendingCell = defendingCell;
    this.connection = getConnection();
  }

  /**
   * Gets the connection between the two cells.

   * @return the connection
   */
  public Connection getConnection() {
    //If in the same row, check if the attacker is on the left or right
    if (attackingCell.cellRowPosition() == defendingCell.cellRowPosition()) {
      if (attackingCell.cellColPosition() < defendingCell.cellColPosition()) {
        return Connection.LEFT_RIGHT;
      } else {
        return Connection.RIGHT_LEFT;
      }
    } else { //Check if the attacker is above or below
      if (attackingCell.cellRowPosition() < defendingCell.cellRowPosition()) {
        return Connection.UP_DOWN;
      } else {
        return Connection.DOWN_UP;
      }
    }
  }

  /**
   * Checks if the fight needs to happen: Both cells must contain differently colored cards.
   *  If they do, then they fight.

   * @return true if there was a fight and the attacker won
   */
  public boolean resolveBattle() {
    //Confirm both cells have been played to
    // (confirms they aren't hole as well because holes can't be played to
    if (attackingCell.hasBeenPlayed() && defendingCell.hasBeenPlayed()) {
      //Confirm both cells are different colors
      if (attackingCell.getCard().getColor() != defendingCell.getCard().getColor()) {
        return fight();
      }
    }
    return false;
  }

  /**
   * The cells fight, if the attacker wins flip the other card otherwise do nothing.
   */
  private boolean fight() {
    //Using the connection, compare values, if the attacker wins,
    // call flipCard on the defending cell
    if (connection == Connection.LEFT_RIGHT) {
      if (defendingCell.getCard().getLeft().getValue()
              < attackingCell.getCard().getRight().getValue()) {
        defendingCell.flipCard();
        return true;
      }
    } else if (connection == Connection.RIGHT_LEFT) {
      if (defendingCell.getCard().getRight().getValue()
              < attackingCell.getCard().getLeft().getValue()) {
        defendingCell.flipCard();
        return true;
      }
    } else if (connection == Connection.UP_DOWN) {
      if (defendingCell.getCard().getUp().getValue()
              < attackingCell.getCard().getDown().getValue()) {
        defendingCell.flipCard();
        return true;
      }
    } else { //Connection is DOWN_UP
      if (defendingCell.getCard().getDown().getValue()
              < attackingCell.getCard().getUp().getValue()) {
        defendingCell.flipCard();
        return true;
      }
    }
    return false;
  }

  /**
   * Get the defendingCell.

   * @return the defendingCell
   */
  public Cell getDefendingCell() {
    return defendingCell;
  }

}
