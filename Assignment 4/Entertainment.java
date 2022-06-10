import tester.*;

interface IEntertainment {
  //compute the total price of this Entertainment
  double totalPrice();

  //computes the minutes of entertainment of this IEntertainment
  int duration();

  //produce a String that shows the name and price of this IEntertainment
  String format();

  //is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);

  boolean sameMHelper(String name, double price, String genre, int pages, int installments);

  boolean sameTVHelper(String name, double price, int installments, String corporation);

  boolean samePodcastHelper(String name, double price, int installments);
}

abstract class AEntertainment implements  IEntertainment {
  String name;
  double price;
  int installments;

  public AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }

  public double totalPrice() {
    return this.price * this.installments;
  }

  public int duration() {
    return 50 * this.installments;
  }

  public String format() {
    return this.name + ", " + this.price + ".";
  }

  public boolean sameMHelper(String name, double price, String genre, int pages, int installments) {
    return false;
  }

  public boolean sameTVHelper(String name, double price, int installments, String corporation) {
    return false;
  }

  public boolean samePodcastHelper(String name, double price, int installments) {
    return false;
  }
}

class Magazine extends AEntertainment {
  String genre;
  int pages;

  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }

  //computes the minutes of entertainment of this Magazine, (includes all installments)
  @Override
  public int duration() {
    return 5 * this.pages * this.installments;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMHelper(this.name, this.price, this.genre, this.pages, this.installments);
  }

  @Override
  public boolean sameMHelper(String name, double price, String genre, int pages, int installments) {
    return (this.name.equals(name) && this.price == price && this.genre.equals(genre)
        && this.pages == pages && this.installments == installments);
  }
}

class TVSeries extends AEntertainment {
  String corporation;

  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVHelper(this.name, this.price, this.installments, this.corporation);
  }

  public boolean sameTVHelper(String name, double price, int installments, String corporation) {
    return (this.name.equals(name) && this.price == price && this.installments == installments
        && this.corporation.equals(corporation));
  }
}

class Podcast extends AEntertainment {

  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }

  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcastHelper(this.name, this.price, this.installments);
  }

  public boolean samePodcastHelper(String name, double price, int installments) {
    return (this.name.equals(name) && this.price == price && this.installments == installments);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.0, 8);
  IEntertainment magazine = new Magazine("Magazine", 4.20, "generic", 20, 56);
  IEntertainment euphoria = new TVSeries("Euphoria", 2.5, 8, "HBO");
  IEntertainment joe = new Podcast("Joe", 0.0, 9999);

  //testing total price method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001)
      && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
      && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
      && t.checkInexact(this.magazine.totalPrice(), 4.2 * 56, .0001)
      && t.checkInexact(this.euphoria.totalPrice(), 2.5 * 8, .001)
      && t.checkInexact(this.joe.totalPrice(), 0.0, .001);
  }

}