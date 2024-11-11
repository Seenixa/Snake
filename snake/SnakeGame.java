package game.snake;

import java.awt.Color;
import java.awt.Frame;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import game.engine.Action;
import game.engine.Game;
import game.engine.ui.Drawable;
import game.engine.ui.FilledOvalObject;
import game.engine.ui.FilledRectangleObject;
import game.engine.ui.GameFrame;
import game.engine.ui.GameObject;
import game.engine.utils.Pair;
import game.snake.players.DummyPlayer;
import game.snake.players.HumanPlayer;
import game.snake.ui.SnakeCanvas;
import game.snake.utils.Cell;
import game.snake.utils.CellComparator;
import game.snake.utils.SnakeGameState;

/**
 * Represents the snake game (like was on the old Nokia phones).
 * https://en.wikipedia.org/wiki/Snake_(video_game_genre)
 */
public class SnakeGame implements Game<SnakePlayer, Direction>, Drawable {
  
  /** represents an empty cell on the board */
  public static final int EMPTY = 0;
  /** represents a snake cell on the board */
  public static final int SNAKE = 1;
  /** represents a food cell on the board */
  public static final int FOOD = 2;
  /** used for the head of the snake on the GUI */
  public static final int HEAD = 3;
  /** represents the score for a food */
  public static final int FOODSCORE = 100;
  /** represents the score for a move */
  public static final int IDLE = -1;
  
  /** static variable for the LEFT direction */
  public static final Direction LEFT = new Direction(0, -1);
  /** static variable for the UP direction */
  public static final Direction UP = new Direction(-1, 0);
  /** static variable for the RIGHT direction */
  public static final Direction RIGHT = new Direction(0, 1);
  /** static variable for the DOWN direction */
  public static final Direction DOWN = new Direction(1, 0);
  /** array of possible directions */
  public static final Direction[] DIRECTIONS = new Direction[] {LEFT, UP, RIGHT, DOWN};
  
  /** maximal width and height of the table */
  public static final int MAXSIZE = 100000;
  
  /** map for the command line view of cells */
  public static final HashMap<Integer, String> TILES;
  
  static {
    TILES = new HashMap<Integer, String>();
    TILES.put(EMPTY, " ");
    TILES.put(SNAKE, "#");
    TILES.put(FOOD, "O");
    TILES.put(HEAD, "@");
  }
  
  private final SnakeCanvas canvas;
  
  private final long seed;
  private final int n, m;
  private final Random random;
  
  private final int startLength = 3;
  private boolean isFinished = false;
  
  private final long timeout;
  private final SnakePlayer[] players;
  private final long[] remainingTimes;
  private final int[] idle;
  private int currentPlayer = 0;
  private final SnakeGameState[] gameState;
  private final SnakeGameState[] playerGameState;
  private final TreeSet<Cell> freeCells;
  
  private final boolean isReplay;
  private final String[] playerClassNames;
  private final PrintStream errStream;
  
  public SnakeGame(PrintStream errStream, boolean isReplay, String[] params) {
    if (params.length != 5) {
      errStream.println("required parameters for the game are:");
      errStream.println("\t- random seed       : controls the sequence of the random numbers");
      errStream.println("\t- num of rows (n)   : size of board");
      errStream.println("\t- num of columns (m): size of board");
      errStream.println("\t- timeout           : play-time for a player in milliseconds");
      errStream.println("\t- player class      : player class");
      System.exit(1);
    }
    this.errStream = errStream;
    this.isReplay = isReplay;
    
    this.seed = Long.parseLong(params[0]);
    this.n = Integer.parseInt(params[1]);
    this.m = Integer.parseInt(params[2]);
    this.timeout = Long.parseLong(params[3]) * 1000000;
    this.random = new Random(seed);
    if (MAXSIZE <= n || MAXSIZE <= m) {
      throw new RuntimeException("Table max size is at most " + MAXSIZE + " instead of " + n + " or " + m);
    }
    canvas = new SnakeCanvas(n, m, this);
    
    players = new SnakePlayer[1];
    playerClassNames = new String[players.length];
    remainingTimes = new long[players.length];
    gameState = new SnakeGameState[players.length];
    playerGameState = new SnakeGameState[players.length];
    idle = new int[players.length];
    for (int i = 0; i < players.length; i++) {
      playerClassNames[i] = params[i + 4];
      remainingTimes[i] = timeout;
      idle[i] = 0;
    }
    freeCells = new TreeSet<Cell>(CellComparator.newInstance());
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        freeCells.add(new Cell(i, j));
      }
    }
    
    //TODO: copy these things if we want more players!
    int[][] board = new int[n][m];
    LinkedList<Cell> snake = new LinkedList<Cell>();
    for (int j = m/2; j < m/2+startLength; j++) {
      Cell cell = new Cell(n/2, j);
      snake.add(cell);
      board[cell.i][cell.j] = SNAKE;
      freeCells.remove(cell);
    }
    Direction currentDirection = LEFT;
    
    Cell food = getFood(freeCells, random);
    board[food.i][food.j] = FOOD;
    freeCells.remove(food);
    gameState[currentPlayer] = new SnakeGameState(board, snake, currentDirection, 0);
    playerGameState[currentPlayer] = new SnakeGameState(gameState[currentPlayer]);
  }
  
  private static final Cell getFood(TreeSet<Cell> cells, Random random) {
    Cell food = null;
    int idx = random.nextInt(cells.size());
    for (Cell c : cells) {
      if (idx == 0) {
        food = c;
      }
      idx--;
    }
    return food;
  }

  @Override
  public long getTimeout() {
    return timeout;
  }

  @Override
  public List<Pair<Constructor<? extends SnakePlayer>, Object[]>> getPlayerConstructors() throws Exception {
    List<Pair<Constructor<? extends SnakePlayer>, Object[]>> result = new LinkedList<Pair<Constructor<? extends SnakePlayer>, Object[]>>();
    for (int idx = 0; idx < playerClassNames.length; idx++) {
      Class<? extends SnakePlayer> clazz = Class.forName(DummyPlayer.class.getName()).asSubclass(SnakePlayer.class);
      if (isReplay) {
        errStream.println("Game is in replay mode, Player: " + idx + " is the DummyPlayer, but was: " + playerClassNames[idx]);
      } else {
        clazz = Class.forName(playerClassNames[idx]).asSubclass(SnakePlayer.class);
      }
      Random r = new Random(seed);
      result.add(new Pair<Constructor<? extends SnakePlayer>, Object[]>(clazz.getConstructor(SnakeGameState.class, int.class, Random.class), new Object[] {playerGameState[idx], idx, r}));
    }
    return result;
  }

  @Override
  public void setPlayers(List<Pair<? extends SnakePlayer, Long>> playersAndTimes) throws Exception {
    int idx = 0;
    for (Pair<? extends SnakePlayer, Long> playerAndTime : playersAndTimes) {
      players[idx] = playerAndTime.first;
      remainingTimes[idx] -= playerAndTime.second;
      if (players[idx] instanceof HumanPlayer) {
        remainingTimes[idx] = Long.MAX_VALUE - playerAndTime.second - 10;
      }
      
      // check color hacking
      if (players[idx] != null && players[idx].color != idx) {
        int color = players[idx].color;
        remainingTimes[idx] = 0;
        Field field = SnakePlayer.class.getDeclaredField("color");
        field.setAccessible(true);
        field.set(players[idx], idx);
        field.setAccessible(false);
        errStream.println("Illegal color (" + color + ") was set for player: " + players[idx]);
      }
      idx ++;
    }
    currentPlayer = 0;
  }

  @Override
  public SnakePlayer[] getPlayers() {
    return players;
  }

  @Override
  public SnakePlayer getNextPlayer() {
    return players[currentPlayer];
  }

  @Override
  public boolean isValid(Direction action) {
    return action == null || action.isValid();
  }

  @Override
  public void setAction(SnakePlayer player, Direction action, long time) {
    if (action != null && action.isValid() && gameState[player.color].direction.perpendicular(action)) {
      gameState[player.color].direction = action;
      playerGameState[player.color].direction = action;
    }
    remainingTimes[player.color] -= time;
    // check timeout
    if (remainingTimes[player.color] <= 0) {
      isFinished = true;
      return;
    }
    
    // get heads and tail
    Cell head = gameState[player.color].getHead();
    Cell newHead = head.apply(gameState[player.color].direction);
    Cell tail = gameState[player.color].getTail();
    
    // remove tail
    gameState[player.color].removeTail();
    gameState[player.color].setValueAt(tail, EMPTY);
    playerGameState[player.color].removeTail();
    playerGameState[player.color].setValueAt(tail, EMPTY);
    freeCells.add(tail);
    
    // check collisions
    if (!gameState[player.color].isOnBoard(newHead) || gameState[player.color].getValueAt(newHead) == SNAKE) {
      gameState[player.color].addTail(tail);
      gameState[player.color].setValueAt(tail, SNAKE);
      playerGameState[player.color].addTail(tail);
      playerGameState[player.color].setValueAt(tail, SNAKE);
      freeCells.remove(tail);
      isFinished = true;
      return;
    }
    freeCells.remove(newHead);
   
    // check food collision
    if (gameState[player.color].getValueAt(newHead) == FOOD) {
      gameState[player.color].score += FOODSCORE;
      playerGameState[player.color].score += FOODSCORE;
      
      // restore tail
      gameState[player.color].addTail(tail);
      gameState[player.color].setValueAt(tail, SNAKE);
      playerGameState[player.color].addTail(tail);
      playerGameState[player.color].setValueAt(tail, SNAKE);
      freeCells.remove(tail);
      idle[player.color] = 0;
      
      // snake has maximal length
      if (gameState[player.color].getSize() == n*m - 1) {
        isFinished = true;
        return;
      }
      // put new food
      Cell food = getFood(freeCells, random);
      gameState[player.color].setValueAt(food, FOOD);
      playerGameState[player.color].setValueAt(food, FOOD);
      freeCells.remove(food);
    }
    
    // add new head
    gameState[player.color].addHead(newHead);
    gameState[player.color].setValueAt(newHead, SNAKE);
    playerGameState[player.color].addHead(newHead);
    playerGameState[player.color].setValueAt(newHead, SNAKE);
    
    // check idle
    if (2*n*m < idle[player.color]) {
      isFinished = true;
      return;
    }
    idle[player.color] ++;
    gameState[player.color].score += IDLE;
    playerGameState[player.color].score += IDLE;
    
    currentPlayer = (currentPlayer + 1) % players.length;
  }

  @Override
  public long getRemainingTime(SnakePlayer player) {
    return remainingTimes[player.color];
  }

  @Override
  public boolean isFinished() {
    return isFinished;
  }

  @Override
  public double getScore(SnakePlayer player) {
    return gameState[player.color].score;
  }

  @Override
  public Class<? extends Action> getActionClass() {
    return Direction.class;
  }
  
  @Override
  public final String toString() {
    return gameState[currentPlayer].toString();
  }

  @Override
  public Frame getFrame() {
    String iconPath = "/game/engine/ui/resources/icon-game.png";
    return new GameFrame("Snake", iconPath, canvas);
  }

  @Override
  public List<GameObject> getGameObjects() {
    LinkedList<GameObject> gos = new LinkedList<GameObject>();
    for (int i = 0; i < gameState[0].board.length; i++) {
      for (int j = 0; j < gameState[0].board[i].length; j++) {
        int x = (int) Math.round(canvas.multiplier * (j + 0.1));
        int y = (int) Math.round(canvas.multiplier * (i + 0.1));
        int w = (int) Math.round(canvas.multiplier * 0.8);
        int h = (int) Math.round(canvas.multiplier * 0.8);
        if (gameState[0].board[i][j] == FOOD) {
          gos.add(new FilledOvalObject(x, y, w, h, new Color(219, 68, 55)));
        }
        if (gameState[0].board[i][j] == SNAKE) {
          if (gameState[0].snake.peekFirst().same(i, j)) {
            gos.add(new FilledRectangleObject(x, y, w, h, Color.gray));
          } else {
            gos.add(new FilledRectangleObject(x, y, w, h, Color.lightGray));
          }
        }
      }
    }
    return gos;
  }

}
