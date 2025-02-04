package cs3500.threetrios.model;

/**
 * Connections are used for Battles between two cells.
 * The two cells are the "attacking" cell and the "defending" cell.
 * The connection is represented by the following:
 *    attacker -> defender
 * The "attacking" cell is the cell that was flipped or placed.
 * The "defending" cell is the cell adjacent to the attacker.
 * Example: The attacker is above the defender, thus their connection is UP_DOWN.
 */
public enum Connection {
  LEFT_RIGHT,
  RIGHT_LEFT,
  UP_DOWN,
  DOWN_UP;
}
