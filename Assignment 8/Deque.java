import tester.*;
import java.util.function.Predicate;

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  abstract int size(int count);

  public void addNext(T data) {
    new Node<T>(data, this.next, this);
  }

  abstract T removeThis();

  abstract ANode<T> find(Predicate<T> pred);

}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    this.data = data;
    this.next = next;
    this.prev = prev;
    if (next == null || prev == null) {
      throw new IllegalArgumentException();
    }
    next.prev = this;
    prev.next = this;
  }

  public int size(int count) {
    return 1 + this.next.size(count);
  }

  public T removeThis() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.data;
  }

  public ANode<T> find(Predicate<T> pred) {
    if (pred.test(data)) {
      return this;
    } else {
      return this.next.find(pred);
    }
  }
}

class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.prev = this;
    this.next = this;
  }

  public int size(int count) {
    if (count > 0) {
      return 0;
    } else {
      return this.next.size(1);
    }
  }

  public T removeThis() {
    throw new RuntimeException();
  }

  public ANode<T> find(Predicate<T> pred) {
    return this;
  }
}

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  public Deque(Sentinel<T> header) {
    this.header = header;
  }

  public int size() {
    return this.header.size(0);
  }

  public void addAtHead(T data) {
    this.header.addNext(data);
  }

  public void addAtTail(T data) {
    this.header.prev.addNext(data);
  }

  public T removeFromHead() {
    return this.header.next.removeThis();
  }

  public T removeFromTail() {
    return this.header.prev.removeThis();
  }

  public ANode<T> find(Predicate<T> pred) {
    return this.header.next.find(pred);
  }

  public void removeNode(ANode<T> aNode) {
    aNode.removeThis();
  }

}

class IsNeg implements Predicate<Integer> {
  public boolean test(Integer num) {
    return num < 0;
  }
}

class IsString implements Predicate<String> {
  String str;

  IsString(String str) {
    this.str = str;
  }

  public boolean test(String s) {
    return s.compareTo(this.str) == 0;
  }
}

class ExamplesDeque {
  Deque<String> deque1 = new Deque<String>();

  Sentinel<String> str1 = new Sentinel<String>();
  Node<String> abc = new Node<String>("abc", str1, str1);
  Node<String> bcd = new Node<String>("bcd",str1, abc);
  Node<String> cde = new Node<String>("cde", str1, bcd);
  Node<String> def = new Node<String>("def", str1, cde);
  Deque<String> deque2 = new Deque<String>(str1);

  Sentinel<Integer> int1 = new Sentinel<Integer>();
  Node<Integer> i2510 = new Node<Integer>(2510, int1, int1);
  Node<Integer> i1 = new Node<Integer>(1, int1, i2510);
  Node<Integer> i420 = new Node<Integer>(420, int1, i1);
  Node<Integer> ineg = new Node<Integer>(-100, int1, i420);
  Deque<Integer> deque3 = new Deque<Integer>(int1);

  void init() {
    deque1 = new Deque<String>();

    str1 = new Sentinel<String>();
    abc = new Node<String>("abc", str1, str1);
    bcd = new Node<String>("bcd",str1, abc);
    cde = new Node<String>("cde", str1, bcd);
    def = new Node<String>("def", str1, cde);
    deque2 = new Deque<String>(str1);

    int1 = new Sentinel<Integer>();
    i2510 = new Node<Integer>(2510, int1, int1);
    i1 = new Node<Integer>(1, int1, i2510);
    i420 = new Node<Integer>(420, int1, i1);
    ineg = new Node<Integer>(-100, int1, i420);
    deque3 = new Deque<Integer>(int1);
  }

  boolean testSize(Tester t) {
    init();

    return t.checkExpect(deque1.size(), 0)
      && t.checkExpect(deque2.size(), 4)
      && t.checkExpect(deque3.size(), 4);
  }

  void testAddAtHead(Tester t) {
    init();

    t.checkExpect(deque1.header.next, deque1.header);
    t.checkExpect(deque2.header.next, abc);

    deque1.addAtHead("test");
    deque2.addAtHead("zab");

    t.checkExpect(deque1.header.next, new Node<String>("test", deque1.header, deque1.header));
    t.checkExpect(deque2.header.next, new Node<String>("zab", abc, deque2.header));
  }

  void testAddAtTail(Tester t) {
    init();

    t.checkExpect(deque1.header.prev, deque1.header);
    t.checkExpect(deque2.header.prev, def);

    deque1.addAtTail("test");
    deque2.addAtTail("efg");

    t.checkExpect(deque1.header.prev, new Node<String>("test", deque1.header, deque1.header));
    t.checkExpect(deque2.header.prev, new Node<String>("efg", str1, def));
  }

  void testRemoveFromHead(Tester t) {
    init();

    t.checkExpect(deque1.header.next, deque1.header);
    t.checkExpect(deque2.header.next, abc);

    t.checkException(new RuntimeException(), deque1, "removeFromHead");
    t.checkExpect(deque2.removeFromHead(), "abc");

    t.checkExpect(deque1.header.next, deque1.header);
    t.checkExpect(deque2.header.next, bcd);
  }

  void testRemoveFromTail(Tester t) {
    init();

    t.checkExpect(deque1.header.prev, deque1.header);
    t.checkExpect(deque2.header.prev, def);

    t.checkException(new RuntimeException(), deque1, "removeFromTail");
    t.checkExpect(deque2.removeFromTail(), "def");

    t.checkExpect(deque1.header.prev, deque1.header);
    t.checkExpect(deque2.header.prev, cde);
  }

  void testFind(Tester t) {
    init();

    t.checkExpect(deque1.find(new IsString("apples")), deque1.header);
    t.checkExpect(deque2.find(new IsString("bcd")), bcd);
    t.checkExpect(deque3.find(new IsNeg()), ineg);
  }

  void testRemoveNode(Tester t) {
    init();

    t.checkExpect(deque1.header.next, deque1.header);
    t.checkExpect(deque2.header.prev, def);


    try {
      deque1.removeNode(deque1.header);
    } catch (Exception e) {
      System.out.println("");
    }
    deque2.removeNode(def);

    t.checkExpect(deque1.header.next, deque1.header);
    t.checkExpect(deque2.header.prev, cde);
  }

}