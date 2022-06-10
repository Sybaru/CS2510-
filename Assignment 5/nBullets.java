import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;

class MyGame extends World {
  int width;
  int height;
  int numBullets;
  int currBullets;
  int score;
  boolean randomCircles;
  boolean welcomeScreen;
  boolean newShot;
  boolean bulletsExist;
  ILoShip ships;
  ILoBullets bullets;

  MyGame(int numBullets) {
    this.width = 500;
    this.height = 500;
    this.randomCircles = false;
    this.welcomeScreen = true;
    this.ships = new MtLoShip();
    this.newShot = false;
    this.bullets = new MtLoBullet();
    this.numBullets = numBullets;
    this.currBullets = 0;
    this.bulletsExist = false;
  }

  MyGame(boolean randomCircles, ILoShip ships, boolean newShot, ILoBullets bullets,
      int numBullets, int currBullets, boolean bulletsExist) {
    this.width = 500;
    this.height = 500;
    this.randomCircles = randomCircles;
    this.welcomeScreen = false;
    this.ships = ships;
    this.newShot = newShot;
    this.bullets = bullets;
    this.numBullets = numBullets;
    this.currBullets = currBullets;
    this.bulletsExist = bulletsExist;
  }

  @Override
  public WorldScene makeScene() {
    //Make a new scene.
    WorldScene scene = new WorldScene(this.width, this.height);

    if (this.welcomeScreen ) {
      scene = this.addWelcomeMessage(scene);
    }

    if (!this.welcomeScreen) {
      scene = this.addInfoToScene(scene);
    }
    if ( this.randomCircles ) {
      this.addRandomCirclesToScene();
    }
    if (this.newShot) {
      this.shooting();
    }

    this.deleteOffScreen();

    this.bullets = this.bullets.move();

    this.bullets.checkBulCollision(this.ships);

    this.bullets = this.bullets.explode();

    this.ships = this.ships.delete();

    this.bulletsExist = this.bullets.exist();

    scene = this.bullets.drawBullets(scene);

    scene = this.ships.drawShips(scene);

    return scene;
  }

  WorldScene addWelcomeMessage(WorldScene scene) {
    return scene.placeImageXY( new TextImage("Game will start shortly.", Color.green), 250,250);
  }

  public void addRandomCirclesToScene() {
    //Generate random coordinates between 0 and this.WIDTH (non inclusive)
    boolean makeCircle = Math.random() < 0.15; 
    boolean movingRight = Math.random() < 0.5; 
    int randY = (new Random()).nextInt(this.height / 2);
    Ship tempCircle;

    if (movingRight) {
      tempCircle = new Ship(10, randY, movingRight, false);
    } else {
      tempCircle = new Ship(470, randY, movingRight, false);
    }
    if (makeCircle) {
      this.ships = new LoShips(tempCircle, this.ships);
    }

    this.ships = this.ships.move();
  }

  @Override
  //This method gets called every tickrate seconds ( see bellow in example class).
  public MyGame onTick() {
    return this.addRandomCircles().incrementGameTick();
  }

  public MyGame addRandomCircles() {
    return new MyGame(true, this.ships, this.newShot, this.bullets,
        this.numBullets, this.currBullets, this.bulletsExist);
  }

  public MyGame incrementGameTick() {
    return new MyGame(this.randomCircles, this.ships, this.newShot,
        this.bullets, this.numBullets, this.currBullets, this.bulletsExist);
  }

  public MyGame onKeyEvent(String key) {
    //did we press the space update the final tick of the game by 10. 
    if (key.equals(" ") && this.currBullets < this.numBullets) {
      return new MyGame(false, this.ships, true, this.bullets,
          this.numBullets, this.currBullets + 1, this.bulletsExist);
    } else if (key.equals(" ")) {
      return new MyGame(false, this.ships, false, this.bullets,
          this.numBullets, this.currBullets + 1, this.bulletsExist);
    } else {
      return this;
    }
  }

  public void deleteOffScreen() {
    this.bullets = this.bullets.deleteOffScreen(); 
    this.score = score += 1;

    //this.ships = this.ships.deleteOffScreen();
  }

  //Check to see if we need to end the game.
  @Override
  public WorldEnd worldEnds() {
    if (this.currBullets >= this.numBullets && !this.bulletsExist) {
      return new WorldEnd(true, this.makeEndScene());
    } else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(this.width, this.height);
    return endScene.placeImageXY( new TextImage("Game Over", Color.red), 250, 250);

  }

  public void shooting() {
    if (this.newShot) {
      this.newShot = false;
      this.bullets = new LoBullet(new Bullet(), this.bullets);
    }

  }

  WorldScene addInfoToScene(WorldScene scene) {
    return scene.placeImageXY( new TextImage("Bullets: " + Integer.toString(
        this.numBullets - this.currBullets) 
    + "  Score: " + Integer.toString(this.score += 1) , Color.black),100, 20);
  }

}

class ExamplesMyWorldProgram {
  boolean testBigBang(Tester t) {
    MyGame world = new MyGame(10);
    //width, height, tickrate = 0.5 means every 0.5 seconds the onTick method will get called.
    return world.bigBang(500, 500, 1.0 / 28.0);
  }
}

interface ILoBullets {
  ILoBullets move();

  WorldScene drawBullets(WorldScene scene);

  void checkBulCollision(ILoShip ship);

  ILoBullets explode();

  ILoBullets deleteOffScreen();

  boolean exist();
}

class Bullet {
  int x;
  int y;
  double angle; // stored in radians
  int explosion;
  boolean hit;

  Bullet(int x, int y, double angle, int explosion, boolean hit) {
    this.x = x;
    this.y = y;
    this.angle = angle;
    this.explosion = explosion;
    this.hit = hit;
  }

  Bullet() { // constructor for initial shot
    this.x = 250;
    this.y = 470;
    this.angle = (Math.PI / 2);
    this.explosion = 1;
    this.hit = false;

  }

  public Bullet bulletMove() {
    int tempX = (int)Math.round(Math.cos(this.angle) * 10);
    int tempY = (int)Math.round(Math.sin(this.angle) * -10);
    return new Bullet(this.x + tempX, this.y + tempY, this.angle, this.explosion, this.hit);

  }

  public WorldScene drawBullet(WorldScene scene) {
    int size = this.explosion + 5;
    if (size > 15) {
      size = 10;
    }
    return scene.placeImageXY( new CircleImage(size, OutlineMode.SOLID,
        Color.pink), this.x, this.y);
  }

  public void checkBulCollision(ILoShip ship) {
    int size = this.explosion + 5;
    if (size > 15) {
      size = 10;
    }
    if (ship.comparePos(this.x, this.y, size)) {
      this.hit = true;
    }
  }

  public ILoBullets explode(ILoBullets bullets) {
    return this.explodeHelper(bullets, 0);
  }

  public ILoBullets explodeHelper(ILoBullets bullets, int curr) {
    if (this.explosion > 6) {
      this.explosion = 6;
    }
    if (curr < this.explosion + 1) {
      Bullet temp = new Bullet(this.x, this.y, Math.toRadians(curr * (360 /
          (this.explosion + 1))), this.explosion + 1, false);
      return new LoBullet(temp, this.explodeHelper(bullets, curr + 1));
    }  else {
      return bullets;
    }
  }

  public boolean offScreen() {
    return (this.x < 0 || this.x > 500 || this.y < 0 || this.y > 500);
  }

}

class LoBullet implements ILoBullets {
  Bullet first;
  ILoBullets rest;
  int score;

  LoBullet(Bullet first, ILoBullets rest) {
    this.first = first;
    this.rest = rest;
    this.score = 0;
  }

  public ILoBullets move() {
    return new LoBullet(this.first.bulletMove(), this.rest.move());
  }

  public WorldScene drawBullets(WorldScene scene) {
    WorldScene temp = this.first.drawBullet(scene);
    return this.rest.drawBullets(temp);
  }

  public void checkBulCollision(ILoShip ship) {
    this.first.checkBulCollision(ship);
    this.rest.checkBulCollision(ship);
    this.score += 1;
  }



  public ILoBullets explode() {
    if (this.first.hit) {
      return this.first.explode(this.rest);
    } else {
      return new LoBullet(this.first, this.rest.explode());
    }
  }

  public ILoBullets deleteOffScreen() {
    if (this.first.offScreen()) {
      return this.rest.deleteOffScreen();
    } else {
      return new LoBullet(this.first, this.rest.deleteOffScreen());
    }
  }

  public boolean exist() {
    return true;
  }
}

class MtLoBullet implements ILoBullets {
  MtLoBullet(){
    
  }

  public ILoBullets move() {
    return this;
  }

  public WorldScene drawBullets(WorldScene scene) {
    return scene;
  }

  public void checkBulCollision(ILoShip ship) {
    // needed for interface
  }

  public ILoBullets explode() {
    return this;
  }

  public ILoBullets deleteOffScreen() {
    return this;
  }

  public boolean exist() {
    return false;
  }

}

interface ILoShip {
  ILoShip move();

  WorldScene drawShips(WorldScene scene);

  boolean comparePos(int x, int y, int size);

  ILoShip delete();

}

class Ship {  
  int x;
  int y;
  boolean movingRight;
  boolean hit;

  Ship(int x, int y, boolean movingRight, boolean hit) {
    this.x = x;
    this.y = y;
    this.movingRight = movingRight;    
    this.hit = hit;
  }

  public int moveX() {
    if (movingRight) {
      return this.x + 10;
    } else {
      return this.x - 10;
    }
  }

  public Ship move() {
    return new Ship(this.moveX(), this.y, this.movingRight, this.hit);
  }

  public WorldScene drawship(WorldScene scene) {
    return scene.placeImageXY( new CircleImage(20, OutlineMode.SOLID, Color.cyan), this.x, this.y);
  }

  public boolean comparePos(int x, int y, int size) {
    double dist = Math.hypot(this.x - x, this.y - y);
    if (dist <= 18 + size) {
      this.hit = true;
      return true;
    } else {
      return false;
    }
  }
}

class LoShips implements ILoShip {
  Ship first;
  ILoShip rest;

  LoShips(Ship first, ILoShip rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoShip move() {
    return new LoShips(this.first.move(), this.rest.move());
  }

  public WorldScene drawShips(WorldScene scene) {
    WorldScene temp = this.first.drawship(scene);
    return this.rest.drawShips(temp);
  }

  public boolean comparePos(int x, int y, int size) {
    if (this.first.comparePos(x, y, size)) {
      this.first.comparePos(x,  y, size);
      return true;
    } else {
      return this.rest.comparePos(x, y, size);
    }
  }

  public ILoShip delete() {
    if (this.first.hit) {
      return this.rest;
    } else {
      return new LoShips(this.first, this.rest.delete());
    }
  }
}

class MtLoShip implements ILoShip {
  MtLoShip() {
  
  }

  public ILoShip move() {
    return this;
  }

  public WorldScene drawShips(WorldScene scene) {
    return scene;
  }

  public boolean comparePos(int x, int y, int size) {
    return false;
  }

  public ILoShip delete() {
    return this;
  }
}






