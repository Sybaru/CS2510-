import tester.Tester;

//represents a stitching motif
interface IMotif {

  //determines the average difficulty of motif(s)
  double averageDifficulty();

  //counts how many motif(s) are in an embroidery piece
  int count(int count);

  //determines a string of info about a motif
  String embroideryInfo();

  //determines if a motif is a group motif
  boolean isGroup();
}

//represents a list of stitching motifs
interface  ILoMotif extends IMotif {

  //determines if the list is empty
  boolean isEmpty();

}

//represents an empty list of motifs
class MtLoMotif implements ILoMotif {

  /* TEMPLATE:
  Fields:
  n/a
  Methods:
  ... this.isEmpty() ... boolean
  ... this.isGroup() ... boolean
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
  */

  public boolean isEmpty() {
    return true;
  }

  public boolean isGroup() {
    return false;
  }

  public double averageDifficulty() {
    return 0;
  }

  public int count(int count) {
    return 0;
  }

  public String embroideryInfo() {
    return ".";
  }
}

//represents a non-empty list of motifs
class ConsLoMotif implements  ILoMotif {
  IMotif first;
  ILoMotif rest;

  /* TEMPLATE:
  Fields:
  ... this.first ... --IMotif
  ... this.rest ... --ILoMotif
  Methods:
  ... this.isEmpty() ... boolean
  ... this.isGroup() ... boolean
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
   */

  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean isEmpty() {
    return false;
  }

  public boolean isGroup() {
    return false;
  }

  public double averageDifficulty() {
    return (this.first.averageDifficulty() + this.rest.averageDifficulty());
  }

  public int count(int count) {
    return count + this.first.count(0) + this.rest.count(count);
  }

  public String embroideryInfo() {
    if (this.first.isGroup()) {
      return this.first.embroideryInfo();
    }
    else if (this.rest.isEmpty()) {
      return (this.first.embroideryInfo() + this.rest.embroideryInfo());
    }
    else {
      return (this.first.embroideryInfo() + ", " + this.rest.embroideryInfo());
    }
  }
}

//represents an embroidery piece
class EmbroideryPiece {
  String name;
  IMotif motif;

  /* TEMPLATE:
  Fields:
  ... this.name ... --String
  ... this.motif ... --IMotif
  Methods:
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
   */

  EmbroideryPiece( String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }

  public int count() {
    return this.motif.count(0);
  }

  public double averageDifficulty() {
    if (this.count() == 0) {
      return 0;
    }
    else {
      return (this.motif.averageDifficulty() / this.count());
    }
  }

  public String embroideryInfo() {
    return (this.name + ": " + this.motif.embroideryInfo());
  }
}

//represents a cross stitch motif
class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  /* TEMPLATE:
  Fields:
  ... this.description ... --String
  ... this.difficulty ... --double
  Methods:
  ... this.isGroup() ... boolean
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
   */

  CrossStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  public double averageDifficulty() {
    return this.difficulty;
  }

  public int count(int count) {
    return 1;
  }

  public String embroideryInfo() {
    return (this.description + " (cross stitch)");
  }

  public boolean isGroup() {
    return false;
  }

}

//represents a chain stitch motif
class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  /* TEMPLATE:
  Fields:
  ... this.description ... --String
  ... this.difficulty ... --double
  Methods:
  ... this.isGroup() ... boolean
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
   */

  ChainStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  public double averageDifficulty() {
    return this.difficulty;
  }

  public int count(int count) {
    return 1;
  }

  public boolean isGroup() {
    return false;
  }

  public String embroideryInfo() {
    return (this.description + " (chain stitch)");
  }
}

//represents a group of motifs
class GroupMotif implements  ILoMotif {
  String description;
  ILoMotif motifs;

  /* TEMPLATE:
  Fields:
  ... this.description ... --String
  ... this.motifs ... --ILoMotif
  Methods:
  ... this.isEmpty() ... boolean
  ... this.isGroup() ... boolean
  ... this.averageDifficulty() ... double
  ... this.count(int) ... int
  ... this.embroideryInfo() ... String
  Methods for Fields:
  n/a
   */

  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  public boolean isEmpty() {
    return false;
  }

  public boolean isGroup() {
    return true;
  }

  public double averageDifficulty() {
    return this.motifs.averageDifficulty();
  }

  public int count(int count) {
    return this.motifs.count(count) + 0;
  }

  public String embroideryInfo() {
    return this.motifs.embroideryInfo();
  }
}

class ExamplesEmbroidery {
  ILoMotif empty = new MtLoMotif();


  IMotif daisy = new CrossStitchMotif("daisy", 3.2);
  IMotif poppy = new ChainStitchMotif("poppy", 4.75);
  IMotif rose = new CrossStitchMotif("rose", 5.0);
  ILoMotif flower2 = new ConsLoMotif(this.daisy, this.empty);
  ILoMotif flower1 = new ConsLoMotif(this.rose, new ConsLoMotif(this.poppy, this.flower2));
  IMotif flowers = new GroupMotif("flowers", this.flower1);
  IMotif bird = new CrossStitchMotif("bird", 4.5);
  IMotif tree = new ChainStitchMotif("tree", 3.0);
  ILoMotif nature2 = new ConsLoMotif(this.flowers, empty);
  ILoMotif nature1 = new ConsLoMotif(this.bird, new ConsLoMotif(this.tree, this.nature2));
  IMotif nature = new GroupMotif("nature", nature1);
  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", this.nature);

  IMotif apple = new CrossStitchMotif("apple", 3.2);
  IMotif banana = new ChainStitchMotif("banana", 4.75);
  IMotif pear = new CrossStitchMotif("pear", 5.0);
  ILoMotif fruit2 = new ConsLoMotif(this.apple, this.empty);
  ILoMotif fruit1 = new ConsLoMotif(this.pear, new ConsLoMotif(this.banana, this.fruit2));
  IMotif fruit = new GroupMotif("flowers", this.fruit1);
  EmbroideryPiece fruits = new EmbroideryPiece("Fruits", this.fruit);


  boolean testAverageDifficulty(Tester t) {
    return t.checkExpect(this.pillowCover.averageDifficulty(), 4.09)
      && t.checkExpect(this.fruits.averageDifficulty(), 4.316);
  }

  boolean testisEmpty(Tester t) {
    return t.checkExpect(this.flower1.isEmpty(), false)
      && t.checkExpect(this.empty.isEmpty(), true);
  }

  boolean testIsGroup(Tester t) {
    return t.checkExpect(this.fruit.isGroup(), true)
      && t.checkExpect(this.empty.isGroup(), false);
  }

  boolean testCount(Tester t) {
    return t.checkExpect(this.fruits.count(), 3)
      && t.checkExpect(this.pillowCover.count(), 6);
  }

  boolean testEmbroideryInfo(Tester t) {
    String t1 = "Fruits: pear(cross stitch), banana(chain stitch), apple(cross stitch).";
    return t.checkExpect(this.fruits.embroideryInfo(), t1)
      && t.checkExpect(this.empty.embroideryInfo(), ".");
  }


}
