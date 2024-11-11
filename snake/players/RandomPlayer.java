package game.snake.players;

import java.util.Random;

import game.snake.Direction;
import game.snake.SnakeGame;
import game.snake.SnakePlayer;
import game.snake.utils.SnakeGameState;

/**
 * Every time chooses a random direction as an action.
 */
public class RandomPlayer extends SnakePlayer {

  public RandomPlayer(SnakeGameState gameState, int color, Random random) {
    super(gameState, color, random);
  }

  @Override
  public Direction getAction(long remainingTime) {
    Direction action = SnakeGame.DIRECTIONS[random.nextInt(SnakeGame.DIRECTIONS.length)];
    return action;
  }

}
