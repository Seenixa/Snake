package game.snake;

import game.engine.Action;
import game.snake.utils.Cell;

/**
 * Represents the direction of the snake on the board and the possible actions 
 * of the players.
 */
public class Direction extends Cell implements Action {
  private static final long serialVersionUID = -2917323766303032272L;
  /**
   * Creates a direction by the specified components.
   * @param i vertical direction {-1,0,1}
   * @param j horizontal direction {-1,0,1}
   */
  public Direction(int i, int j) {
    super(i, j);
  }
  /**
   * Returns true iff the one of the components of the direction is 0 and
   * the other is 1 or -1.
   * @return true if the direction is valid
   */
  public boolean isValid() {
    return Math.abs(i) + Math.abs(j) == 1;
  }
  /**
   * Copy constructor.
   * @param d clones the specified value
   */
  public Direction(Direction d) {
    super(d);
  }
  /**
   * Returns true iff the specified direction is perpendicular to the current.
   * @param d to be checked
   * @return true iff perpendicular
   */
  public boolean perpendicular(Direction d) {
    return (i * d.i) + (j * d.j) == 0;
  }
  @Override
  public String toString() {
    String dir = "INVALID";
    if (i == 1 && j == 0) dir = "DOWN";
    else if (i == -1 && j == 0) dir = "UP";
    else if (i == 0 && j == 1) dir = "RIGHT";
    else if (i == 0 && j == -1) dir = "LEFT";
    return super.toString() + " " + dir;
  }
}
