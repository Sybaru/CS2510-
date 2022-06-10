import tester.Tester;

//Represents a Picture
interface IPicture {

  //determines the type of picture
  String type();

  //gets the width of a picture
  int getWidth();

  //counts how many shapes are in a picture
  int countShapes();

  //finds the max depth of a picture
  int comboDepth();

  //mirrors a picture
  IPicture mirror();

  //shows how to make the picture
  String pictureRecipe(int depth);

}

//represents operations used on pictures
interface  IOperation extends IPicture{

}

//represents a shape
class Shape implements IPicture {
  String kind;
  int size;

  /* TEMPLATE:
  Fields:
  ... this.kind ... --String
  ... this.size ... --int
  Methods:
  ... this.type() ... String
  ... this.getWidth() ... int
  ... this.countShapes() ... int
  ... this.comboDepth(int) ... int
  ... this.mirror() ... IPicture
  ... this.pictureRecipe(int depth) ... String
  Methods for Fields:
  n/a
  */

  Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }

  public String type() {
    return this.kind;
  }

  public int getWidth() {
    return this.size;
  }

  public int countShapes() {
    return 1;
  }

  public int comboDepth() {
    return 0;
  }

  public IPicture mirror() {
    return this;
  }

  public String pictureRecipe(int depth) {
    return this.kind;
  }
}

class Combo implements  IPicture {
  String name;
  IOperation operation;

  /* TEMPLATE:
  Fields:
  ... this.kind ... --String
  ... this.operation ... --IOperation
  Methods:
  ... this.type() ... String
  ... this.getWidth() ... int
  ... this.countShapes() ... int
  ... this.comboDepth(int) ... int
  ... this.mirror() ... IPicture
  ... this.pictureRecipe(int depth) ... String
  Methods for Fields:
  n/a
  */

  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }

  public String type() {
    return this.name;
  }

  public int getWidth() {
    return this.operation.getWidth();
  }

  public int countShapes() {
    return this.operation.countShapes();
  }

  public int comboDepth() {
    return 0 + this.operation.comboDepth();
  }

  public IPicture mirror() {
    return this.operation.mirror();
  }

  public String pictureRecipe(int depth) {
    if (depth <= 0) {
      return this.name;
    }
    else {
      return this.operation.pictureRecipe(depth - 1);
    }
  }
}

class Scale implements IOperation {
  IPicture picture;

  /* TEMPLATE:
  Fields:
  ... this.picture ... --IPicture
  Methods:
  ... this.type() ... String
  ... this.getWidth() ... int
  ... this.countShapes() ... int
  ... this.comboDepth(int) ... int
  ... this.mirror() ... IPicture
  ... this.pictureRecipe(int depth) ... String
  Methods for Fields:
  n/a
  */

  Scale(IPicture picture) {
    this.picture = picture;
  }

  public String type() {
    return "";
  }

  public int getWidth() {
    return (this.picture.getWidth() * 2);
  }

  public int countShapes() {
    return this.picture.countShapes();
  }

  public int comboDepth() {
    return 1 + this.picture.comboDepth();
  }

  public IPicture mirror() {
    return new Scale(this.picture.mirror());
  }

  public String pictureRecipe(int depth) {
    if (depth <= 1) {
      return ("scale(" + this.picture.type() + ")");
    }
    else {
      return ("scale(" + this.picture.pictureRecipe(depth) + ")");
    }
  }
}

class Beside implements  IOperation {
  IPicture picture1;
  IPicture picture2;

  /* TEMPLATE:
  Fields:
  ... this.picture1 ... --IPicture
  ... this.picture2 ... --IPicture
  Methods:
  ... this.type() ... String
  ... this.getWidth() ... int
  ... this.countShapes() ... int
  ... this.comboDepth(int) ... int
  ... this.mirror() ... IPicture
  ... this.pictureRecipe(int depth) ... String
  Methods for Fields:
  n/a
  */

  Beside(IPicture picture1, IPicture picture2) {
    this.picture1 = picture1;
    this.picture2 = picture2;
  }

  public String type() {
    return "";
  }

  public int getWidth() {
    return (this.picture1.getWidth() + this.picture2.getWidth());
  }

  public int countShapes() {
    return this.picture1.countShapes() + this.picture2.countShapes();
  }

  public int comboDepth() {
    if (this.picture1.comboDepth() > this.picture2.comboDepth()) {
      return 1 + this.picture1.comboDepth();
    }
    else {
      return 1 + this.picture2.comboDepth();
    }
  }

  public IPicture mirror() {
    return new Beside(this.picture2.mirror(), this.picture1.mirror());
  }

  public String pictureRecipe(int d) {
    if (d <= 1) {
      return ("beside(" + this.picture1.type() + ", " + this.picture2.type() + ")");
    }
    else {
      String string1 = this.picture2.pictureRecipe(d) + ")";
      return "beside(" + this.picture1.pictureRecipe(d) + ", " + string1;
    }
  }
}

class Overlay implements  IOperation {
  IPicture topPicture;
  IPicture bottomPicture;

  /* TEMPLATE:
  Fields:
  ... this.topPicture ... --IPicture
  ... this.bottomPicture ... --IPicture
  Methods:
  ... this.type() ... String
  ... this.getWidth() ... int
  ... this.countShapes() ... int
  ... this.comboDepth(int) ... int
  ... this.mirror() ... IPicture
  ... this.pictureRecipe(int depth) ... String
  Methods for Fields:
  n/a
  */

  Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }

  public String type() {
    return "";
  }

  public int getWidth() {
    if (this.topPicture.getWidth() > this.bottomPicture.getWidth()) {
      return this.topPicture.getWidth();
    }
    else {
      return this.bottomPicture.getWidth();
    }
  }

  public int countShapes() {
    return this.topPicture.countShapes() + this.bottomPicture.countShapes();
  }

  public int comboDepth() {
    if (this.topPicture.comboDepth() > this.bottomPicture.comboDepth()) {
      return 1 + this.topPicture.comboDepth();
    }
    else {
      return 1 + this.bottomPicture.comboDepth();
    }
  }

  public IPicture mirror() {
    return new Overlay(this.topPicture.mirror(), this.bottomPicture.mirror());
  }

  public String pictureRecipe(int d) {
    if (d <= 1) {
      return ("overlay(" + this.topPicture.type() + ", " + this.bottomPicture.type() + ")");
    }
    else {
      String string1 = this.bottomPicture.pictureRecipe(d) + ")";
      return ("overlay(" + this.topPicture.pictureRecipe(d) + ", " + string1);
    }
  }
}


class ExamplesPicture {
  IPicture circle = new Shape("circle", 20);
  IPicture square = new Shape("square", 30);
  IPicture bigCircle = new Combo("big circle", new Scale(this.circle));
  IOperation overlay1 = new Overlay(this.square, this.bigCircle);
  IPicture squareOnCircle = new Combo("square on circle", this.overlay1);
  IOperation beside1 = new Beside(this.squareOnCircle, this.squareOnCircle);
  IPicture doubledSquareOnCircle = new Combo("doubled square on circle", this.beside1);
  IPicture circle2 = new Shape("circle", 10);
  IPicture square2 = new Shape("square", 40);
  IPicture bigSquare = new Combo("big square", new Scale(this.square2));
  IOperation overlay2 = new Overlay(this.circle2, this.bigSquare);
  IPicture circleOnSquare = new Combo("circle on square", this.overlay2);
  IOperation beside2 = new Beside(this.bigSquare, this.bigSquare);
  IPicture doubledBigSquare = new Combo("doubled Big Square", this.beside2);

  boolean testType(Tester t) {
    return t.checkExpect(this.circle.type(), "circle")
      && t.checkExpect(this.doubledSquareOnCircle.type(), "doubled square on circle");
  }

  boolean testGetWidth(Tester t) {
    return t.checkExpect(this.square.getWidth(), 30)
      && t.checkExpect(this.overlay2.getWidth(), 80);
  }

  boolean testCountShapes(Tester t) {
    return t.checkExpect(this.square2.countShapes(), 1)
      && t.checkExpect(this.doubledBigSquare.countShapes(), 2);
  }

  boolean testComboDepth(Tester t) {
    return t.checkExpect(this.square.comboDepth(), 0)
      && t.checkExpect(this.doubledBigSquare.comboDepth(), 2);
  }

  boolean testMirror(Tester t) {
    return t.checkExpect(this.circle2.mirror(), this.circle2)
      && t.checkExpect(this.beside1.mirror(), new Beside(this.squareOnCircle, this.squareOnCircle));
  }

  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(this.bigCircle.pictureRecipe(9), "Scale(circle)")
      && t.checkExpect(this.bigSquare.pictureRecipe(0), "big square");
  }
}

