package game.snake.utils;

import java.util.LinkedList;

import game.snake.Direction;
import game.snake.SnakeGame;

/**
 * Represents a cell on a board.
 */
public class Cell {
  /** row index of the cell */
  public final int i;
  /** column index of the cell */
  public final int j;
  /**
   * Constructs a cell by its coordinates.
   * @param i row index
   * @param j column index
   */
  public Cell(int i, int j) {
    this.i = i;
    this.j = j;
  }
  /** 
   * Copy constructor.
   * @param c to be cloned
   */
  public Cell(Cell c) {
    this(c.i, c.j);
  }
  /**
   * Returns true iff the specified parameters represent the same position as the cell's coordinates.
   * @param i row index
   * @param j column index
   * @return true, iff represents the same position
   */
  public boolean same(int i, int j) {
    return this.i == i && this.j == j;
  }
  /**
   * Returns true iff the specified cell represents the same position as the current one.
   * @param c to be checked
   * @return true if represents the same position
   */
  public boolean same(Cell c) {
    return same(c.i, c.j);
  }
  /**
   * Applies the specified direction on the cell (adds the direction parts to the cell's coordinates)
   * @param d to be added
   * @return the result cell
   */
  public Cell apply(Direction d) {
    return new Cell(i + d.i, j + d.j);
  }
  /**
   * Returns the Manhattan distance between the current and the specified cells.
   * @param c calculate the distance to
   * @return Manhattan distance
   */
  public int distance(Cell c) {
    return Math.abs(c.i - i) + Math.abs(c.j - j);
  }
  /**
   * Returns the direction object that points to the specified cell from the current  cell.
   * @param c calculate direction to
   * @return direction to the cell
   */
  public Direction directionTo(Cell c) {
    return new Direction((int)Math.signum(c.i - i), (int)Math.signum(c.j - j));
  }
  /**
   * Returns the list of the neighbors correspond to the current cell.
   * @return list of neighbors
   */
  public LinkedList<Cell> neighbors() {
    LinkedList<Cell> result = new LinkedList<Cell>();
    for (Direction d : SnakeGame.DIRECTIONS) {
      result.add(this.apply(d));
    }
    return result;
  }
  @Override
  public String toString() {
    return i + ", " + j;
  }
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Cell) {
      return same((Cell)obj);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return i * SnakeGame.MAXSIZE + j;
  }
}
