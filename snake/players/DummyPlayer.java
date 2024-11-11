package game.snake.players;

import java.util.Random;

import game.snake.Direction;
import game.snake.SnakePlayer;
import game.snake.utils.SnakeGameState;

/**
 * Used for replaying the games.
 */
public class DummyPlayer extends SnakePlayer {

  public DummyPlayer(SnakeGameState gameState, int color, Random random) {
    super(gameState, color, random);
  }

  @Override
  public Direction getAction(long remainingTime) {
    return null;
  }

}
