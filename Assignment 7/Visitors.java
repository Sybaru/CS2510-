import tester.*;
import java.util.function.BiFunction;
import java.util.function.Function;

interface IArithVisitor<R> {

  R visitUnary(UnaryFormula u);

  R visitBinary(BinaryFormula b);

  R visitConst(Const c);

}

interface IArith {

  <R> R accept(IArithVisitor<R> visitor);

}

class Const implements IArith {
  double num;

  public Const(double num) {
    this.num = num;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

class neg implements Function<Double, Double> {

  public Double apply(Double num) {
    return num * -1;
  }
}

class sqr implements Function<Double, Double> {

  public Double apply(Double num) {
    return num * num;
  }
}

class UnaryFormula implements  IArith {
  Function<Double, Double> func;
  String name;
  IArith child;

  public UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitUnary(this);
  }

}

class plus implements BiFunction<Double, Double, Double> {

  public Double apply(Double num1, Double num2) {
    return num1 + num2;
  }
}

class minus implements BiFunction<Double, Double, Double> {

  public Double apply(Double num1, Double num2) {
    return num1 - num2;
  }
}

class mul implements BiFunction<Double, Double, Double> {

  public Double apply(Double num1, Double num2) {
    return num1 * num2;
  }
}

class div implements BiFunction<Double, Double, Double> {

  public Double apply(Double num1, Double num2) {
    return num1 / num2;
  }
}

class BinaryFormula implements  IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith right;
  IArith left;

  public BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith right, IArith left) {
    this.func = func;
    this.name = name;
    this.right = right;
    this.left = left;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitBinary(this);
  }

}

class EvalVisitor implements IArithVisitor<Double> {

  public Double apply(IArith input) {
    return input.accept(new EvalVisitor());
  }

  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitUnary(UnaryFormula u) {
    return u.func.apply(u.child.accept(new EvalVisitor()));
  }

  public Double visitBinary(BinaryFormula b) {
    return b.func.apply(b.right.accept(new EvalVisitor()), b.left.accept(new EvalVisitor()));
  }
}

class PrintVisitor implements IArithVisitor<String> {

  public String apply(IArith input) {
    return input.accept(new PrintVisitor());
  }

  public String visitUnary(UnaryFormula u) {
    return "(" + u.name + " " + u.child.accept(new PrintVisitor()) + ")";
  }

  public String visitBinary(BinaryFormula b) {
    return "(" + b.name + " " + b.right.accept(new PrintVisitor())
      + " " + b.left.accept(new PrintVisitor()) + ")";
  }

  public String visitConst(Const c) {
    return Double.toString(c.num);
  }
}

class DoublerVisitor implements IArithVisitor<IArith> {

  public IArith apply(IArith input) {
    return input.accept(new DoublerVisitor());
  }

  public IArith visitUnary(UnaryFormula u) {
    return new UnaryFormula(u.func, u.name, u.child.accept(new DoublerVisitor()));
  }

  public IArith visitBinary(BinaryFormula b) {
    return new BinaryFormula(b.func, b.name, b.right.accept(new DoublerVisitor()),
      b.left.accept((new DoublerVisitor())));
  }

  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }
}

class NoNegativeResults implements IArithVisitor<Boolean> {

  public Boolean apply(IArith input) {
    return input.accept(new NoNegativeResults());
  }

  public Boolean visitUnary(UnaryFormula u) {
    return (u.child.accept(new NoNegativeResults())
      && u.accept(new EvalVisitor()) >= 0);
  }

  public Boolean visitBinary(BinaryFormula b) {
    return (b.left.accept(new NoNegativeResults())
      && (b.right.accept(new NoNegativeResults())
      && b.accept(new EvalVisitor()) >= 0));
  }

  public Boolean visitConst(Const c) {
    return (c.num >= 0);
  }
}

class ExamplesVisitors {
  Const zero = new Const(0.0);

  boolean testDoubler(Tester t) {
    return t.checkInexact(new EvalVisitor().apply(zero.accept(new DoublerVisitor()))
      , 0.0, 0.001);
  }

}