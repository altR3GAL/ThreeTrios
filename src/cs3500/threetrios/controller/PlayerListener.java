package cs3500.threetrios.controller;

/**
 * Player listener represents the listeners for objects that observe player actions.
 */
public interface PlayerListener {
  /**
   * listener for when a card is selected to be played and is heard by listener.
   *
   * @param index the index of the selected card.
   */
  void cardChosenHeard(int index);

  /**
   * listener for when a position is chosen for a card to be played and is heard by listener.
   *
   * @param row the row index for the position.
   * @param col the col index for the position.
   */
  void positionChosenHeard(int row, int col);
}
