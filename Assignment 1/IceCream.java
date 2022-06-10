interface IIceCream {

}

class Scooped implements IIceCream {
  String flavor;
  IIceCream more;

  Scooped(IIceCream more, String flavor) {
    this.flavor = flavor;
    this.more = more;
  }
}

class EmptyServing implements IIceCream {
  boolean cone;

  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

class ExamplesIceCream {
  EmptyServing emptyServing = new EmptyServing(false);
  IIceCream mintChip = new Scooped(this.emptyServing, "caramel swirl");
  IIceCream coffee = new Scooped(this.mintChip, "black raspberry");
  IIceCream blackRasp = new Scooped(this.coffee, "coffee");
  IIceCream order1 = new Scooped(this.blackRasp, "mint chip");

  EmptyServing emptyCone = new EmptyServing(true);
  IIceCream chocolate = new Scooped(this.emptyCone, "strawberry");
  IIceCream vanilla = new Scooped(this.chocolate, "vanilla");
  IIceCream order2 = new Scooped(this.vanilla, "chocolate");
}
