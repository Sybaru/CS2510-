interface IBagel {
  boolean sameRecipe(BagelRecipe other);

}

abstract class ABagel implements IBagel {
  double water;
  double flour;
  double yeast;
  double salt;
  double malt;

  public ABagel(double water, double flour, double yeast, double salt, double malt) {
    this.water = water;
    this.flour = flour;
    this.yeast = yeast;
    this.salt = salt;
    this.malt = malt;
    this.validBagel();
  }


  public void validBagel() {
    if (this.flour != this.water) {
      throw new IllegalArgumentException("flour doesn't equal water");
    }
    if (this.yeast != this.malt) {
      throw new IllegalArgumentException("yeast doesn't equal malt");
    }
    if (!within(this.salt, ((.05 * this.flour) - this.yeast))) {
      throw new IllegalArgumentException("salt + yeast doesnt equal 1/20 flour");
    }
  }

  public boolean sameRecipe(BagelRecipe other) {
    return (within(this.flour, other.flour) &&
      within(this.water, other.water) &&
      within(this.malt, other.malt) &&
      within(this.salt, other.salt) &&
      within(this.yeast, other.yeast));
  }

  public boolean within(double thisNum, double that) {
    return (that >= (thisNum - 0.001) && that <= thisNum + 0.001);
  }

}

class BagelRecipe extends ABagel {

  public BagelRecipe(double water, double flour, double yeast, double salt, double malt) {
    super(water, flour, yeast, salt, malt);
  }

  public BagelRecipe(double flour, double yeast) {
    super(flour, flour, yeast, (flour * 0.05) - yeast, yeast);
  }

  public BagelRecipe(double flour, double yeast, double salt) {
    super(flour * 4.25,
        flour * 4.25, (yeast / 48) * 5, (salt / 48) * 10, (yeast / 48) * 5);
  }
}

class ExamplesBagel {
  IBagel bagel1 = new BagelRecipe(5.0, 2.0);

}
