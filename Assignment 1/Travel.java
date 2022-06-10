interface IHousing {

}

class Hut implements IHousing {
  int capacity;
  int population;

  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
}

class Inn implements IHousing {
  String name;
  int capacity;
  int population;
  int stalls;

  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.stalls = stalls;
    this.population = population;
  }
}

class Castle implements IHousing {
  String name;
  String familyName;
  int population;
  int carriageHouse;

  Castle(String name, String familyName, int population, int carriageHouse) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriageHouse = carriageHouse;
  }
}

interface ITransportation {

}

class Horse implements ITransportation {
  IHousing from;
  IHousing to;
  String name;
  String color;

  Horse(IHousing from, IHousing to, String name, String color) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.color = color;
  }
}

class Carriage implements ITransportation {
  IHousing from;
  IHousing to;
  int tonnage;

  Carriage(IHousing from, IHousing to, int tonnage) {
    this.from = from;
    this.to = to;
    this.tonnage = tonnage;
  }
}

class ExamplesTravel {
  IHousing hovel = new Hut(5, 1);
  IHousing winterfell = new Castle("Winterfell", "Stark", 500, 6);
  IHousing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  IHousing hut2 = new Hut(10, 8);
  IHousing castle2 = new Castle("Boston", "Smith", 900000, 100);
  IHousing inn2 = new Inn("The Westin Copley Place", 1000, 700, 200);

  ITransportation horse1 = new Horse(this.hovel, this.crossroads, "Horsey", "White");
  ITransportation carriage1 = new Carriage(this.winterfell, this.crossroads, 5);
  ITransportation horse2 = new Horse(this.hut2, this.inn2, "Bojack", "Brown");
  ITransportation carriage2 = new Carriage(this.inn2, this.winterfell, 1000);
}
