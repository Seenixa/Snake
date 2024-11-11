package game.snake;

import java.util.List;
import java.util.Random;

import game.engine.Player;
import game.engine.utils.Pair;
import game.snake.utils.SnakeGameState;

/**
 * Represents an abstract class of the players in the Snake game.
 */
public abstract class SnakePlayer implements Player<Direction> {
  /** contains the current state of the game */
  public final SnakeGameState gameState;
  /** the color (id) of the player */
  public final int color;
  /** random number generator */
  public final Random random;
  /**
   * Creates a Snake player instance
   * @param gameState state of the game
   * @param color color/id of the player
   * @param random random number generator
   */
  public SnakePlayer(SnakeGameState gameState, int color, Random random) {
    this.gameState = gameState;
    this.color = color;
    this.random = random;
  }
  
  /**
   * Abstract method that controls the snake on  the board.
   * @param remainingTime the remaining time of the player
   * @return the next action of the player
   */
  public abstract Direction getAction(long remainingTime);

  @Override
  public final Direction getAction(List<Pair<Integer, Direction>> prevActions, long[] remainingTimes) {
    return getAction(remainingTimes[0]);
  }

  @Override
  public final int getColor() {
    return color;
  }
  
  @Override
  public final String toString() {
    return getClass().getCanonicalName();
  }

}
