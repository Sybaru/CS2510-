//CS 2510 assignment 3 problem 2

import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import javalib.worldcanvas.WorldCanvas;

// to represent a tree
interface ITree {

  //to draw the tree
  WorldImage draw();

  //to check if any parts of the tree are drooping
  boolean isDrooping();

  // to combine two trees
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, 
      ITree otherTree);

  // rotates a tree
  ITree rotate(double theta);

  // helps rotate a tree
  ITree rotateHelper(double theta);

  // returns the width of a tree
  double getWidth();

  // returns the width of the left side of a tree
  double getLeftWidth();

  // returns the width of the right side of a tree
  double getRightWidth();

}

// to represent a leaf on a tree
class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  public Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }


  // draws the leaf
  public WorldImage draw() {
    return new CircleImage(size, OutlineMode.SOLID, color);
  }

  // returns false, as a leaf cant droop
  public boolean isDrooping() {
    return false;
  }

  // returns this, as a rotates circle is the same
  public ITree rotate(double theta) {
    return this;
  }

  // returns this, as a rotated circle is the same
  public ITree rotateHelper(double theta) {
    return this;
  }

  // returns the leaf combined with another tree
  public ITree combine(int leftLength, int rightLength, double leftTheta,
      double rightTheta, ITree otherTree) {
    ITree other = otherTree.rotate(rightTheta);
    return new Branch(leftLength, rightLength, leftTheta,
        rightTheta, this.rotate(leftTheta), other);
  }

  // returns the diameter of the leaf
  public double getWidth() {
    return this.getRightWidth() - this.getLeftWidth();
  }

  // returns the radius of the leaf
  public double getRightWidth() {
    return size;
  }

  // returns the radius of the leaf but negative
  public double getLeftWidth() {
    return -1 * size;
  }
}

// to represent a stem on a tree
class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }

  /*
    TEMPLATE
    FIELDS
    ... this.length ...   -- int
    ... this.theta ...    -- double
    ... this.tree ...     -- ITree

    METHODS
    ... this.isDrooping() ...    -- boolean
    ... this.draw() ...    -- WorldImage
    ... this.getWidth() ...   -- double
    ... this.getRightWidth() ...   -- double
    ... this.getLeftWidth() ...   -- double

    METHODS FOR FIELDS
    ... this.rotate(double) ...    --ITree
    ... this.rotateHelper(double) ...   -- ITree
    ... this.combine(int leftLength, int rightLength, double leftTheta,
        double rightTheta, ITree otherTree) ...   -- ITree
   */

  // checks if the tree is drooping
  public boolean isDrooping() {
    Positive posTheta = new Positive(theta);
    double remainder = posTheta.output;
    if ((remainder >= 0) && (remainder <= 180)) {
      return tree.isDrooping();
    } else {
      return true;
    }
  }


  // rotates the stem
  public ITree rotate(double theta) {
    return this.rotateHelper(theta - 90);
  }

  // helps rotate the stem
  public ITree rotateHelper(double theta) {
    return new Stem(this.length, this.theta + theta, tree.rotateHelper(theta));
  }


  // draws the stem
  public WorldImage draw() {
    double thetaRad = theta * (Math.PI / 180);
    Posn pos = new Posn((int) (length * Math.cos(thetaRad) * -1),
        ((int) (length * Math.sin(thetaRad))));
    WorldImage stem = new VisiblePinholeImage(new LineImage(
        pos, Color.black).movePinhole(pos.x / 2, pos.y / 2));
    return new VisiblePinholeImage( new OverlayImage(
        tree.draw().movePinhole(pos.x, pos.y), stem));
  }

  // combines the stem with another tree
  public ITree combine(int leftLength, int rightLength, double leftTheta, 
      double rightTheta, ITree otherTree) {
    ITree stem = this.rotate(leftTheta);
    ITree other = otherTree.rotate(rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, stem, other);
  }

  // finds the width of the stem
  public double getWidth() {
    if (this.theta == 90) {
      return this.tree.getWidth();
    } else {
      return this.getRightWidth() - this.getLeftWidth();
    }
  }

  // finds the right side width of the stem
  public double getRightWidth() {
    if (theta < 90) {
      return (Math.cos(Math.toRadians(this.theta)) * this.length) + this.tree.getRightWidth();
    } else {
      return 0;
    }
  }

  // finds the left side width of the stem
  public double getLeftWidth() {
    if (theta > 90) {
      return (Math.cos(Math.toRadians(this.theta)) * this.length) + this.tree.getLeftWidth();
    } else {
      return 0;
    }
  }
}

// represents a Branch in a tree
class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  public Branch(int leftLength, int rightLength, double leftTheta,
      double rightTheta, ITree left, ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }

  /*
    TEMPLATE
    FIELDS
    ... this.leftLength ...   -- int
    ... this.rightLength ...   -- int
    ... this.leftTheta ...    -- double
    ... this.rightTheta ...    -- double
    ... this.left ...     -- ITree
    ... this.right ...     -- ITree

    METHODS
    ... this.isDrooping() ...    -- boolean
    ... this.draw() ...    -- WorldImage
    ... this.getWidth() ...   -- double
    ... this.getRightWidth() ...   -- double
    ... this.getLeftWidth() ...   -- double

    METHODS FOR FIELDS
    ... this.rotate(double) ...    --ITree
    ... this.rotateHelper(double) ...   -- ITree
    ... this.combine(int leftLength, int rightLength, double leftTheta,
        double rightTheta, ITree otherTree) ...   -- ITree
   */

  // checks if the branch is drooping anywhere
  public boolean isDrooping() {
    Divide leftDivide = new Divide(leftTheta);
    double remainderLeft = leftDivide.output;
    Divide rightDivide = new Divide(rightTheta);
    double remainderRight = rightDivide.output;

    if ((remainderLeft >= 0) && (remainderLeft <= 180) && (remainderRight >= 0)
        && (remainderRight <= 180)) {
      return (left.isDrooping() || right.isDrooping()); 
    } else {
      return true;
    }
  }

  // rotates the branch
  public ITree rotate(double theta) {
    double posTheta = new Positive(theta).output;
    double change = posTheta - 90;
    return new Branch(this.leftLength, this.rightLength, this.leftTheta + change,
        this.rightTheta + change, this.left.rotateHelper(change), this.right.rotateHelper(change));
  }

  // helps rotate the branch
  public ITree rotateHelper(double theta) {
    return new Branch(this.leftLength, this.rightLength, this.leftTheta +
        theta, this.rightTheta + theta, this.left.rotateHelper(theta),
        this.right.rotateHelper(theta));
  }

  // draws the branch
  public WorldImage draw() {
    Stem leftStem = new Stem(leftLength, leftTheta, left);
    Stem rightStem = new Stem(rightLength, rightTheta, right);
    return new VisiblePinholeImage(new OverlayImage(leftStem.draw(), rightStem.draw()));
  }

  // combines the branch with another ITree
  public ITree combine(int leftLength, int rightLength, double leftTheta,
      double rightTheta, ITree otherTree) {
    ITree other = otherTree.rotate(rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        this.rotate(leftTheta), other);
  }

  // finds the width of the branch
  public double getWidth() {
    return this.getRightWidth() - this.getLeftWidth();
  }

  // finds the right side width of the branch
  public double getRightWidth() { 
    return (Math.cos(Math.toRadians(this.rightTheta)) * 
        this.rightLength) + this.right.getRightWidth();

  }

  // finds the left side width of the branch
  public double getLeftWidth() {
    return (Math.cos(Math.toRadians(this.leftTheta)) * this.leftLength)
        + this.left.getLeftWidth();
  }
}


// takes large degrees and makes them less than 360
class Divide {
  double input;
  double output;

  Divide(double input) {
    this.input = input;

    if (input >= 360) {
      this.output = input % (360);
    } else {
      this.output = input;
    }
  }

  /*
    TEMPLATE
    FIELDS
    ... this.input ...   -- double
    ... this.output ...   -- double
  */
} 

// takes negative degrees and makes them positive
class Positive {
  double input;
  double output;

  Positive(double input) {
    this.input = input;
    if (input <= 0) {
      Positive temp = new Positive(input + 360);
      this.output = temp.output;
    } else {
      this.output = input;
    }
  }

  /*
    TEMPLATE
    FIELDS
    ... this.input ...   -- double
    ... this.output ...   -- double
  */
}

class ExamplesTree {
  ITree circle = new Leaf(25, Color.red);
  ITree testBranch1 = new Branch(100, 100, 90, 45, circle, circle);
  ITree testBranch2 = new Branch(100, 100, 60, 200, circle, circle);
  ITree testBranch3 = new Branch(100, 100, 150, 360, circle, circle);
  ITree testStem1 = new Stem(40, 100, circle);

  ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));

  ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));

  ITree stem1 = new Stem(40, 90, tree1);

  ITree stem2 = new Stem(50, 90, tree2);

  ITree branch = new Branch(40, 50, 150, 30, tree1, tree2);



  boolean testDrawTree(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(tree1.combine(40, 50, 150, 30, tree2).draw(), 250, 250))
        && c.show();
  }

  boolean testIsDrooping1(Tester t) {
    return t.checkExpect(this.branch.isDrooping(), false);
  }

  boolean testIsDrooping2(Tester t) {
    return t.checkExpect(this.testBranch2.isDrooping(), true);
  }

  boolean testIsDrooping3(Tester t) {
    return t.checkExpect(this.testBranch3.isDrooping(), false);
  }
}