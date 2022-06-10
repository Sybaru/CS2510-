import java.util.Comparator;
import tester.*;


interface IFunc<T> {

  // applies function with argument
  boolean apply(T arg);
}

class HasStudent implements IFunc<Course> {
  Student student;

  HasStudent(Student student) {
    this.student = student;
  }

  // applies hasT function to student with a course as an argument
  public boolean apply(Course c) {
    return  c.students.hasT(this.student, new CompareStudent());
  }
}

interface IList<T> {

  // does ormap, running a boolean function for everything in a list and
  // returning an or with all results
  boolean ormap(IFunc<T> f);

  // checks if a list has object c based on the comparator order
  public boolean hasT(T c, Comparator<T> order);

  // counts things in a list based on input function
  int countByFunc(IFunc<T> f);

}

class MtList<T> implements IList<T> {

  // returns false because empty
  public boolean ormap(IFunc<T> f) {
    return false;
  }

  // returns false because empty
  public boolean hasT(T c, Comparator<T> order) {
    return false;
  }

  // returns 0 because empty
  public int countByFunc(IFunc<T> f) {
    return 0;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // returns the comparator based on this.first or the rest of the list
  public boolean hasT(T c, Comparator<T> order) {
    return order.compare(this.first, c) == 0 || this.rest.hasT(c, order);
  }

  // does ormap on the whole list
  public boolean ormap(IFunc<T> f) {
    return f.apply(this.first) || this.rest.ormap(f);
  }

  // counts items in the list based on input function
  public int countByFunc(IFunc<T> f) {
    if (f.apply(this.first)) {
      return this.rest.countByFunc(f) + 1;
    } else {
      return this.rest.countByFunc(f);
    }
  }
}

class CompareStudent implements Comparator<Student> {

  // compares two students by id number
  public int compare(Student s1, Student s2) {
    return s1.id - s2.id;
  }
}


class Instructor {
  String name;
  IList<Course> courses;

  public Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // determines whether the given Student is in more than one of this Instructor’s Courses
  boolean dejavu(Student s) {
    return this.courses.countByFunc(new HasStudent(s)) > 1;
  }
}

class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  public Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    prof.courses = new ConsList<Course>(this, prof.courses);
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;

  public Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // enrolls a student in a course, adding the course to the student's list of courses and
  // the student to the course's list of students
  public void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.students = new ConsList<Student>(this, c.students);
  }

  // checks if the other student shares a class with this student
  public boolean classmates(Student c) {
    return this.courses.ormap(new HasStudent(c));
  }
}

class ExamplesRegistrar {

  //students
  Student s1 = new Student("Dave", 1);
  Student s2 = new Student("John", 2);
  Student s3 = new Student("Jennifer", 3);
  Student s4 = new Student("Alan", 4);
  Student s5 = new Student("Connor", 5);
  Student s6 = new Student("Bruh", 6);

  //instructors
  Instructor i1 = new Instructor("David");
  Instructor i2 = new Instructor("Angela");

  //courses
  Course c1 = new Course("Fundies", i1);
  Course c2 = new Course("Calculus", i2);
  Course c3 = new Course("Biology", i2);
  Course c4 = new Course("Cooking", i1);
  Course c5 = new Course("Bruhology", i1);

  //lists of students
  IList<Student> empty = new MtList<Student>();
  IList<Student> listC1 = new ConsList<Student>(s1, new ConsList<Student>(s2, empty));
  IList<Student> listC2 = new ConsList<Student>(s3, new ConsList<Student>(s4,
      new ConsList<Student>(s5, empty)));
  IList<Student> listC3 = new ConsList<Student>(s1, new ConsList<Student>(s3,
      new ConsList<Student>(s5, empty)));
  IList<Student> listC4 = new ConsList<Student>(s2, empty);

  //lists of courses
  IList<Course> mtCourse = new MtList<Course>();
  IList<Course> cs1 = new ConsList<Course>(c1, new ConsList<Course>(c2, mtCourse));
  IList<Course> cs2 = new ConsList<Course>(c1, new ConsList<Course>(c4, mtCourse));
  IList<Course> cs3 = new ConsList<Course>(c2, new ConsList<Course>(c3, mtCourse));
  IList<Course> cs4 = new ConsList<Course>(c2, mtCourse);
  IList<Course> cs5 = new ConsList<Course>(c2, new ConsList<Course>(c3, mtCourse));


  void initial() {

    //initialize lists of students in courses
    c1.students = listC1;
    c2.students = listC2;
    c3.students = listC3;
    c4.students = listC4;
    c5.students = new ConsList<Student>(s6, empty);

    //initialize lists of courses in students
    s1.courses = cs1;
    s2.courses = cs2;
    s3.courses = cs3;
    s4.courses = cs4;
    s5.courses = cs5;
    s6.courses = new ConsList<Course>(c5, mtCourse);
  }

  // test Enroll method
  void testEnroll(Tester t) {
    initial();
    t.checkExpect(s4.courses, new ConsList<Course>(c2, mtCourse));
    t.checkExpect(c5.students, new ConsList<Student>(s6, empty));
    s4.enroll(c5);
    t.checkExpect(c5.students, new ConsList<Student>(s4, new ConsList<Student>(s6, empty)));
    t.checkExpect(s4.courses, new ConsList<Course>(c5, new ConsList<Course>(c2, mtCourse)));
  }

  // test classmates method
  boolean testClassmates(Tester t) {
    initial();

    return t.checkExpect(s1.classmates(s2), true)
      && t.checkExpect(s2.classmates(s5), false);
  }

  // test dejavu method
  boolean testDejaVu(Tester t) {
    initial();
    return t.checkExpect(i2.dejavu(s3), true)
      && t.checkExpect(i1.dejavu(s1), false)
      && t.checkExpect(i2.dejavu(s2), false);
  }

  boolean testHasStudent(Tester t) {
    initial();
    return t.checkExpect(new HasStudent(s6).apply(c5), true)
      && t.checkExpect(new HasStudent(s6).apply(c4), false);
  }

  // test countbyfunc menthod
  boolean testCountByFunc(Tester t) {
    initial();
    return t.checkExpect(cs2.countByFunc(new HasStudent(s2)), 2)
      && t.checkExpect(cs4.countByFunc(new HasStudent(s2)), 0)
      && t.checkExpect(mtCourse.countByFunc(new HasStudent(s5)), 0);
  }

  // test HasT
  boolean testHasT(Tester t) {
    initial();
    return t.checkExpect(listC2.hasT(s4, new CompareStudent()), true)
      && t.checkExpect(listC2.hasT(s6, new CompareStudent()), false)
      && t.checkExpect(empty.hasT(s4, new CompareStudent()), false);
  }

  // test ormap
  boolean testOrMap(Tester t) {
    initial();
    return t.checkExpect(cs1.ormap(new HasStudent(s1)), true)
      && t.checkExpect(mtCourse.ormap(new HasStudent(s1)), false)
      && t.checkExpect(cs4.ormap(new HasStudent(s1)), false);
  }

  // test compareStudent
  boolean testCompareStudent(Tester t) {
    initial();
    return t.checkExpect(new CompareStudent().compare(s1, s1), 0)
      && t.checkExpect(new CompareStudent().compare(s1, s2), -1);
  }





}