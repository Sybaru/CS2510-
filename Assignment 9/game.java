import java.util.*;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;

import javalib.worldimages.*;

// Represents a single square of the game area
class Cell {
  ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.RED,
      Color.CYAN, Color.BLUE, Color.GREEN, Color.magenta, Color.ORANGE));


  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell() {
    int randInt = (new Random()).nextInt(this.colors.size());
    this.color = colors.get(randInt);
    this.flooded = false;
  }

  Cell(Color c) {
    this.color = c;
  }

  // EFFECT: returns a WorldImage of the cell
  WorldImage drawCell() {
    return new RectangleImage(20, 20, OutlineMode.SOLID, this.color);
  }

  // EFFECT: floods adjacent cells of the right color if this cell is flooded
  void flood(Color c) {
    if (this.flooded) {
      if (this.y > 0) {
        this.top.floodHelper(c);
      }
      if (this.x > 0) {
        this.left.floodHelper(c);
      }
      if (this.y < FloodItWorld.BOARD_SIZE - 1) {
        this.bottom.floodHelper(c);
      }
      if (this.x < FloodItWorld.BOARD_SIZE - 1) {
        this.right.floodHelper(c);
      }
    }
  }

  // EFFECT: floods the cell if the color matches and the cell is not flooded
  void floodHelper(Color c) {
    if (this.color == c && !this.flooded) {
      this.flooded = true;
    }
  }

  // returns the right and bottom neighbors of the cell if they have not already been selected
  // and if they exist
  ArrayList<Cell> waterNeighbors(ArrayList<Cell> done) {
    ArrayList<Cell> temp = new ArrayList<Cell>();
    if (this.y < FloodItWorld.BOARD_SIZE - 1 && !done.contains(this.bottom)) {
      temp.add(this.bottom);
    }
    if (this.x < FloodItWorld.BOARD_SIZE - 1 && !done.contains(this.right)) {
      temp.add(this.right);
    }
    return temp;
  }
}




class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;
  ArrayList<Cell> currCells;
  ArrayList<Cell> doneCells;

  static int BOARD_SIZE;

  public Color currColor;

  public int clicks;

  public int moves;

  public int ticks;

  public boolean win = false;


  FloodItWorld() {
    this.BOARD_SIZE = 22;

    createBoard(22);
  }

  FloodItWorld(int size) {
    this.BOARD_SIZE = size;

    createBoard(size);
    System.out.println("");
  }

  // EFFECT: acts as a constructor for the board, creating a board based on the inputted size
  public void createBoard(int size) {
    ArrayList<Cell> tempBoard = new ArrayList<Cell>();
    for (int i = 0; i < size * size; i++) {
      tempBoard.add(new Cell());
    }
    this.board = new ArrayList<Cell>(tempBoard);
    for (int i = 0; i < size * size; i++) {
      this.fillNeighbors(this.board.get(i), i);
    }
    Cell zero = this.board.get(0);
    zero.flooded = true;
    zero.flood(zero.color);
    this.currColor = (zero.color);
    this.clicks = 0;
    this.ticks = 0;
    this.moves = (int)Math.round(1.75 * size) + 3;
    this.currCells = new ArrayList<Cell>(Arrays.asList(zero));
    this.doneCells = new ArrayList<Cell>();
  }

  // EFFECT: fills the neighboring cell values for each cell
  public void fillNeighbors(Cell c, int cellIndex) {
    if (cellIndex == 0) {
      c.flooded = true;
    }
    if (cellIndex > this.BOARD_SIZE - 1) {
      c.top = this.board.get(cellIndex - this.BOARD_SIZE);
    }
    if (cellIndex % this.BOARD_SIZE != 0) {
      c.left = this.board.get(cellIndex - 1);
    }
    if (cellIndex < this.BOARD_SIZE * this.BOARD_SIZE - this.BOARD_SIZE) {
      c.bottom = this.board.get(cellIndex + this.BOARD_SIZE);
    }
    if (cellIndex % this.BOARD_SIZE < this.BOARD_SIZE - 1) {
      c.right = this.board.get(cellIndex + 1);
    }
    c.x = cellIndex % this.BOARD_SIZE;
    c.y = cellIndex / this.BOARD_SIZE;
  }

  // makes the scene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 500);
    int index = 0;
    WorldImage border = new RectangleImage(this.BOARD_SIZE * 20 + 10,
        this.BOARD_SIZE * 20 + 10, OutlineMode.SOLID, Color.black);
    WorldImage drawnBoard = new EmptyImage();

    for (int i = 0; i < this.BOARD_SIZE; i++) {
      WorldImage temp = new EmptyImage();
      for (int j = 0; j < this.BOARD_SIZE; j++) {
        temp = new BesideImage(temp, this.board.get(index).drawCell());
        index = index + 1;
      }
      drawnBoard = new AboveImage(drawnBoard, temp);
    }

    drawnBoard = new OverlayImage(drawnBoard, border);
    String score = this.clicks + "/" + this.moves;
    WorldImage scoreImage = new TextImage(score, 20 ,Color.black);
    scene.placeImageXY(drawnBoard, 225, 225);
    scene.placeImageXY(scoreImage, 250, 465);
    if (this.win) {
      scene.placeImageXY(new TextImage("You Win!", 20, Color.black), 250, 485);
    } else if (this.clicks > this.moves) {
      scene.placeImageXY(new TextImage("You lose. :(", 20, Color.black), 250, 485);
    }
    WorldImage time = new TextImage("time:", 20, Color.black);
    time = new AboveImage(time, new TextImage(String.valueOf(this.ticks), 20, Color.black));
    scene.placeImageXY(time, 475, 100);
    return scene;
  }

  // EFFECT: runs every tick, starts the waterfall effect and checks for win
  public void onTick() {
    if (this.currCells.size() > 0) {
      this.waterfall();
    }
    if (this.winCheck() && this.clicks <= this.moves) {
      this.win = true;
    }
    if (!win) {
      this.ticks = this.ticks + 1;
    }
  }

  // EFFECT: animates the waterfall effect of the game
  public void waterfall() {
    ArrayList<Cell> tempCells = new ArrayList<Cell>();
    for (Cell c: this.currCells) {
      if (c.flooded) {
        c.color = this.currColor;
      }
      tempCells.addAll(c.waterNeighbors(this.doneCells));
      this.doneCells.addAll(c.waterNeighbors(this.doneCells));
    }
    this.currCells = new ArrayList<Cell>();
    this.currCells.addAll(tempCells);
  }

  // EFFECT: handles mouse clicks, checks for validity of cell clicks
  public void onMouseClicked(Posn pos) {
    int xCo = 0;
    int yCo = 0;
    if (pos.x > 5 && pos.x < 445 && pos.y > 5 && pos.y < 445) {
      xCo = (pos.x - 5) / 20;
      yCo = (pos.y - 5) / 20;
    }
    Color tempColor = this.board.get(yCo * 22 + xCo).color;
    if (this.currColor != tempColor) {
      this.clicks = this.clicks + 1;
      this.currColor = tempColor;
      this.flood();
    }
    this.currCells = new ArrayList<Cell>(Arrays.asList(this.board.get(0)));
    this.doneCells = new ArrayList<Cell>();
  }

  //EFFECT: sets all cells that are flooded to be flooded = true
  public void flood() {
    Iterator<Cell> it = this.board.iterator();
    boolean done = false;
    while (!done) {
      Cell temp = it.next();
      temp.flood(this.currColor);
      if (!it.hasNext()) {
        done = true;
      }
    }
  }

  // EFFECT: handles the reset of the game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      createBoard(this.BOARD_SIZE);
    }
  }

  // checks if win condition is fulfilled
  public boolean winCheck() {
    boolean win = true;
    for (Cell c : this.board) {
      if (!c.flooded) {
        win = false;
      }
    }
    return win;
  }
}

class ExamplesGame {
  void testGame(Tester t) {
    FloodItWorld g = new FloodItWorld();
    g.bigBang(500, 500, 0.035);
  }

  FloodItWorld testGame = new FloodItWorld();
  FloodItWorld testGame2 = new FloodItWorld(1);

  Cell testCell = new Cell(Color.ORANGE);
  Cell testCell2 = new Cell(Color.GREEN);

  void testFillNeighbors(Tester t) {
    Cell zero = testGame.board.get(0);
    Cell twotwo = testGame.board.get(22);
    Cell twothree = testGame.board.get(23);
    Cell twofour = testGame.board.get(24);
    Cell fourfive = testGame.board.get(45);
    Cell one = testGame.board.get(1);
    Cell fef = testGame.board.get(483);

    // check if left border and top border are null
    t.checkExpect(zero.left, null);
    t.checkExpect(zero.top, null);

    // check neighbors for a cell in the middle of the board
    t.checkExpect(twothree.top, one);
    t.checkExpect(twothree.left, twotwo);
    t.checkExpect(twothree.bottom, fourfive);
    t.checkExpect(twothree.right, twofour);

    // check if bottom border and right border are null
    t.checkExpect(fef.right, null);
    t.checkExpect(fef.bottom, null);
  }

  void testDrawCell(Tester t) {
    t.checkExpect(testCell.drawCell(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE));

    t.checkExpect(testCell2.drawCell(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN));
  }

  void testDrawScene(Tester t) {
    FloodItWorld.BOARD_SIZE = 1;
    WorldImage cell1 = testGame2.board.get(0).drawCell();
    WorldImage mt = new EmptyImage();
    WorldImage board = new OverlayImage(
        new AboveImage( mt, new BesideImage(mt, cell1)),
        new RectangleImage(30, 30, OutlineMode.SOLID, Color.black));

    WorldScene scene = new WorldScene(500, 500);
    scene.placeImageXY(board, 225, 225);
    String score = 0 + "/" + 5;
    WorldImage scoreImage = new TextImage(score, 20 ,Color.black);
    scene.placeImageXY(scoreImage, 250, 465);
    WorldImage time = new TextImage("time:", 20, Color.black);
    time = new AboveImage(time, new TextImage("0", 20, Color.black));
    scene.placeImageXY(time, 475, 100);

    t.checkExpect(testGame2.makeScene(), scene);
  }

  void testCellFlood(Tester t) {
    FloodItWorld.BOARD_SIZE = 22;
    Cell testFloodCell = new Cell(Color.black);
    Cell testFloodCell2 = new Cell(Color.BLUE);
    Cell testFloodCell3 = new Cell(Color.BLUE);
    testFloodCell.right = testFloodCell2;
    testFloodCell.bottom = testFloodCell3;
    testFloodCell.flooded = true;
    testFloodCell.x = 0;
    testFloodCell.y = 0;
    testFloodCell.flood(Color.black);
    t.checkExpect(testFloodCell2.flooded, false);
    t.checkExpect(testFloodCell3.flooded, false);
    testFloodCell.flood(Color.BLUE);
    t.checkExpect(testFloodCell2.flooded, true);
    t.checkExpect(testFloodCell3.flooded, true);
  }

  void testCellFloodHelper(Tester t) {
    FloodItWorld.BOARD_SIZE = 22;
    Cell testFloodCell = new Cell(Color.black);
    Cell testFloodCell2 = new Cell(Color.BLUE);
    testFloodCell.right = testFloodCell2;
    testFloodCell.flooded = true;
    testFloodCell2.flooded = false;
    testFloodCell.floodHelper(Color.black);
    t.checkExpect(testFloodCell.flooded, true);
    testFloodCell2.floodHelper(Color.black);
    t.checkExpect(testFloodCell2.flooded, false);
    testFloodCell2.floodHelper(Color.BLUE);
    t.checkExpect(testFloodCell2.flooded, true);
  }

  void testWaterNeighbors(Tester t) {
    FloodItWorld.BOARD_SIZE = 22;
    Cell testFloodCell = new Cell(Color.black);
    Cell testFloodCell2 = new Cell(Color.BLUE);
    Cell testFloodCell3 = new Cell(Color.BLUE);
    testFloodCell.right = testFloodCell2;
    testFloodCell.bottom = testFloodCell3;
    testFloodCell.flooded = true;
    testFloodCell2.flooded = false;
    testFloodCell.x = 0;
    testFloodCell.y = 0;
    ArrayList<Cell> hasCells = new ArrayList<Cell>(Arrays.asList(testFloodCell2, testFloodCell3));
    ArrayList<Cell> noCells = new ArrayList<Cell>();
    t.checkExpect(testFloodCell.waterNeighbors(hasCells),
        new ArrayList<Cell>());
    t.checkExpect(testFloodCell.waterNeighbors(noCells),
        new ArrayList<Cell>(Arrays.asList(testFloodCell2, testFloodCell3)));
  }

  void testCreateBoard(Tester t) {
    FloodItWorld testBoard = new FloodItWorld(22);
    testBoard.onMouseClicked(new Posn(250, 250));
    t.checkExpect(testBoard.clicks, 1);
    testBoard.createBoard(22);
    t.checkExpect(testBoard.clicks, 0);
  }

  void testOnTick(Tester t) {
    FloodItWorld testTick = new FloodItWorld(1);
    FloodItWorld testIncrementTick = new FloodItWorld(22);
    testTick.board.get(0).color = Color.black;
    testTick.currCells.add(testTick.board.get(0));
    t.checkExpect(testTick.ticks, 0);
    t.checkExpect(testTick.win, false);
    t.checkExpect(testTick.board.get(0).color, Color.black);
    t.checkExpect(testIncrementTick.ticks, 0);
    testTick.currColor = Color.BLUE;
    testTick.onTick();
    testIncrementTick.onTick();
    t.checkExpect(testTick.ticks, 0);
    t.checkExpect(testIncrementTick.ticks, 1);
    t.checkExpect(testTick.win, true);
    t.checkExpect(testTick.board.get(0).color, Color.BLUE);
  }

  void testWaterfall(Tester t) {
    FloodItWorld waterfallBoard = new FloodItWorld(1);
    waterfallBoard.board.get(0).color = Color.black;
    waterfallBoard.currColor = Color.BLUE;
    t.checkExpect(waterfallBoard.board.get(0).color, Color.black);
    waterfallBoard.waterfall();
    t.checkExpect(waterfallBoard.board.get(0).color, Color.BLUE);


  }

  void testOnMouse(Tester t) {
    FloodItWorld clickTest = new FloodItWorld();
    clickTest.currColor = Color.BLUE;
    clickTest.board.get(0).color = Color.black;
    t.checkExpect(clickTest.currColor, Color.BLUE);
    clickTest.onMouseClicked(new Posn(6, 6));
    t.checkExpect(clickTest.currColor, Color.black);
  }

  void testOnKey(Tester t) {
    FloodItWorld resetTest = new FloodItWorld(22);
    resetTest.onTick();
    resetTest.onKeyEvent("h");
    t.checkExpect(resetTest.ticks, 1);
    resetTest.onKeyEvent("r");
    t.checkExpect(resetTest.ticks, 0);
  }


  void testWinCheck(Tester t) {
    FloodItWorld noWin = new FloodItWorld(22);
    FloodItWorld win = new FloodItWorld(1);
    t.checkExpect(noWin.winCheck(), false);
    t.checkExpect(win.winCheck(), true);
  }

  void testFloodGame(Tester t) {
    FloodItWorld flooder = new FloodItWorld(2);
    flooder.board.get(0).color = Color.black;
    flooder.board.get(1).color = Color.black;
    flooder.board.get(2).color = Color.black;
    flooder.board.get(3).color = Color.black;
    flooder.currColor = Color.black;
    flooder.flood();
    t.checkExpect(flooder.board.get(0).flooded, true);
    t.checkExpect(flooder.board.get(1).flooded, true);
    t.checkExpect(flooder.board.get(2).flooded, true);
    t.checkExpect(flooder.board.get(3).flooded, true);
  }

}