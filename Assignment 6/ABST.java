import tester.Tester;
import java.util.Comparator;

interface IList<T> {

}

class MtList<T> implements IList<T> {
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

abstract class ABST<T> {
  Comparator<T> order;

  public ABST(Comparator<T> order) {
    this.order = order;
  }

  public int compare(T o1, T o2) {
    return this.order.compare(o1, o2);
  }

  public abstract ABST<T> insert(T item);

  public boolean present(T item) {
    return false;
  }

  public abstract T getLeftmost();

  public abstract T getLeftmostHelper(T curr);

  public abstract ABST<T>  getRight();

  public abstract ABST<T> getRightHelper(T left);

  public abstract boolean sameTree(ABST<T> that);

  public abstract boolean sameLeft(ABST<T> that);

  public abstract boolean sameRight(ABST<T> that);

  public abstract boolean sameNode(T that);

  public abstract boolean sameLeaf(ABST<T> that);

  public abstract boolean sameData(ABST<T> that);

  public abstract IList<T> buildList();
}



class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  public Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  public ABST<T> insert(T item) {
    int comp = this.compare(this.data, item);
    if (comp <= 0) {
      return new Node(this.order, this.data, this.left, this.right.insert(item));
    } else {
      return new Node(this.order, this.data, this.left.insert(item), this.right);
    }
  }

  public boolean present(T item) {
    int comp = this.compare(this.data, item);
    if (comp == 0) {
      return true;
    } else {
      return this.right.present(item) || this.left.present(item);
    }
  }

  public T getLeftmost() {
    return this.getLeftmostHelper(this.data);
  }

  public T getLeftmostHelper(T curr) {
    return this.left.getLeftmostHelper(this.data);
  }

  public ABST<T> getRight() {
    T temp = this.getLeftmost();
    if (compare(this.data, temp) == 0) {
      return this.right;
    } else {
      return new Node(this.order, this.data, this.left.getRightHelper(temp), this.right);
    }
  }

  public ABST<T> getRightHelper(T left) {
    if (compare(this.data, left) == 0) {
      return this.right;
    } else {
      return new Node(this.order, this.data, this.left.getRightHelper(left), this.right);
    }
  }

  public boolean sameNode(T that) {
    return (this.compare(this.data, that) == 0);
  }

  public boolean sameTree(ABST<T> that) {
    if (that.sameNode(this.data)) {
      return that.sameRight(this.right) && that.sameLeft(this.left);
    } else {
      return false;
    }
  }

  public boolean sameLeft(ABST<T> that) {
    return this.left.sameTree(that);
  }

  public boolean sameRight(ABST<T> that) {
    return this.right.sameTree(that);
  }

  public boolean sameLeaf(ABST<T> that) {
    return false;
  }

  public boolean sameData(ABST<T> that) {
    if (this.compare(that.getLeftmost(), this.getLeftmost()) == 0) {
      return (this.getRight().sameData(that.getRight()));
    } else {
      return false;
    }
  }

  public IList<T> buildList() {
    return new ConsList(this.getLeftmost(), this.getRight().buildList());
  }
}

class Leaf<T> extends ABST<T> {

  Leaf(Comparator<T> order) {
    super(order);
  }

  public ABST<T> insert(T item) {
    return new Node(this.order, item, new Leaf(this.order), new Leaf(this.order));
  }

  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  public T getLeftmostHelper(T curr) {
    return curr;
  }

  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  public ABST<T> getRightHelper(T left) {
    return this;
  }

  public boolean sameTree(ABST<T> that) {
    return that.sameLeaf(this);
  }

  public boolean sameRight(ABST<T> that) {
    return false;
  }

  public boolean sameLeft(ABST<T> that) {
    return false;
  }

  public boolean sameNode(T that) {
    return false;
  }

  public boolean sameData(ABST<T> that) {
    return that.sameLeaf(this);
  }

  public boolean sameLeaf(ABST<T> that) {
    return true;
  }

  public IList<T> buildList() {
    return new MtList();
  }
}

class Book {
  String title;
  String author;
  int price;

  public Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

class BooksByTitle implements Comparator<Book> {

  public int compare(Book o1, Book o2) {
    return o1.title.compareTo(o2.title);
  }
}

class BooksByAuthor implements Comparator<Book> {

  public int compare(Book o1, Book o2) {
    return o1.author.compareTo(o2.author);
  }

}

class BooksByPrice implements Comparator<Book> {

  public int compare(Book o1, Book o2) {
    return o1.price - o2.price;
  }

}

class ExamplesBST {
  Book b1 = new Book("title", "author", 200);
  Book b2 = new Book("apple", "author", 300);
  Leaf l1 = new Leaf(new BooksByTitle());
  Node n0 = new Node(new BooksByTitle(), b2, l1, l1);
  Node n1 = new Node(new BooksByTitle(), b1, l1, l1);
  Node n2 = new Node(new BooksByTitle(), b1, n0, l1);
  Node n3 = new Node(new BooksByTitle(), b2, l1, n1);

  ConsList cl1 = new ConsList(b1, new ConsList(b2,new MtList()));

  boolean testInsert(Tester t) {
    return t.checkExpect(n1.insert(b2), new Node(new BooksByTitle(), b1,
      new Node(new BooksByTitle(), b2, l1, l1), l1))
      && t.checkExpect(l1.insert(b1), n1)
      && t.checkExpect(n1.insert(b1), new Node(new BooksByTitle(), b1, l1,
      new Node(new BooksByTitle(), b1, l1, l1)));
  }

  boolean testPresent(Tester t) {
    return t.checkExpect(n1.present(b1), true)
      && t.checkExpect(l1.present(b1), false)
      && t.checkExpect(n1.present(b2), false);
  }

  boolean testGetLeft(Tester t) {
    return t.checkExpect(n2.getLeftmost(), b2)
      && t.checkExpect(n1.getLeftmost(), b1)
      && t.checkException(new RuntimeException("No leftmost item of an empty tree"),
      l1, "getLeftmost");
  }

  boolean testGetRight(Tester t) {
    return t.checkExpect(n2.getRight(), new Node(new BooksByTitle(), b1, l1, l1))
      && t.checkException(new RuntimeException("No right of an empty tree"), l1, "getRight");
  }

  boolean testSameData(Tester t) {
    return t.checkExpect(n2.sameData(n2), true)
      && t.checkExpect(n2.sameData(n1), false)
      && t.checkExpect(l1.sameData(l1), true)
      && t.checkExpect(n3.sameData(n2), true);
  }

  boolean testSameTree(Tester t) {
    return t.checkExpect(n2.sameTree(n2), true)
      && t.checkExpect(n2.sameTree(n1), false)
      && t.checkExpect(l1.sameTree(l1), true)
      && t.checkExpect(n3.sameTree(n2), false);
  }
}