// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
  
  // combine all Strings in this list into one
  String combine();

  // finds the smallest string in a list
  String smallest(String curr);

  //  lowercases the list and sorts it
  ILoString sort();

  // it puts the smallest valued string in a list first
  ILoString removeSmallest(String small);

  // lowercases the strings in a list 
  ILoString listLowerCase();

  //sorts a list based on values are the smallest
  ILoString sortHelper();

  //  checks if a list is sorted
  boolean isSorted();
  
  // checks if a list is sorted
  boolean isSortedHelper(String curr);


  // takes a given list of strings and a new list of string and produces a list where the first,
  // third, fifth... elements are from this list, and the second, fourth, sixth... elements are
  // from the given list
  ILoString interleave(ILoString list);

  // takes two sorted lists and produces a new sorted list which contains all elements from both
  // lists
  ILoString merge(ILoString list);

  //takes two sorted lists and produces a new sorted list which contains all elements from both
  // lists
  ILoString compareMerge(String that, ILoString list);

  //  reverses the order of the list
  ILoString reverse();

  //  helps to reverse the order of the list
  ILoString reverseHelper(ILoString listOld);

  //  helps to find the last element in the list and put it first
  String reverseLast();
  
  // removes the last item in a list of strings 
  ILoString removeLast(); 

  //   checks to see if a list is doubled 
  // (contains pairs of identical strings, that is, the first and second strings are the same,
  // the third and fourth are the same, etc.)
  boolean isDoubledList();
  
  //helps to find if a list contains pairs 
  boolean isDoubledListHelper(String word, boolean doCompare);

  //  finds if a list can be read the same forwards as backwards
  boolean isPalindromeList();

  //  helps isPalindromeList in that it counts how many items are in the list
  int countList(int curr);
  
  // helps isPalindromeList in that it generates the first X amount of items in the list in order
  // to later compare it to the
  // later X amount of items in a list
  ILoString firstX(int curr);
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}

  // combine all Strings in this list into one
  public String combine()  {
    return "";
  }  

  // returns the current String
  public String smallest(String curr) {
    return curr;

  }

  // returns this, since it is already sorted
  public ILoString sort() {
    return this;
  }

  // returns this since there is no "smallest"
  public ILoString removeSmallest(String small) {
    return this;
  }

  // returns this, since there is nothing else to lowercase
  public ILoString listLowerCase() {
    return this;
  }

  // returns this since the list is already is sorted
  public ILoString sortHelper() {
    return this;
  }

  // returns true when the list reaches MtLoString, since it is already sorted
  public boolean isSorted() {
    return true;
  }

  // returns true when the list reaches MtLoString, since it is already sorted
  public boolean isSortedHelper(String curr) {
    return true;
  }

  // returns the already "interleaved" list 
  public ILoString interleave(ILoString list) {
    return list;
  }

  // returns the already merged list
  public ILoString merge(ILoString list) {
    return list;
  }

  // fills out the rest of the list with the other list that is not empty yet
  public ILoString compareMerge(String that, ILoString list) {
    return new ConsLoString(that, list);
  }

  //  returns the already reversed list
  public ILoString reverse() {
    return this;
  }

  // returns the already reversed list 
  public ILoString reverseHelper(ILoString listOld) {
    return this;
  }
  
  // when the list reaches MtLoString, it returns ""
  public String reverseLast() {
    return "";
  }
  
  //
  public ILoString removeLast() {
    return this;
  }

  // when the list reaches MtLoString, it returns true
  public boolean isDoubledList() {
    return true;
  }

  // returns false if we do compare, else it returns true
  public boolean isDoubledListHelper(String word, boolean doCompare) {
    return (!doCompare);
  }

  // when the iteration of the list reaches MtLoString, it returns true
  public boolean isPalindromeList() {
    return true;
  }

  //  returns the current value of the integer
  public int countList(int curr) {
    return curr;
  }

  // returns the list of strings
  public ILoString firstX(int curr) {
    return this;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;  
  }

  /*
     TEMPLATE
     FIELDS:
     ... this.first ...         -- String
     ... this.rest ...          -- ILoString

     METHODS
     ... this.combine() ...     -- String
     ... this.smallest(String curr) ...     -- String
     ... this.removeSmallest(String small) ...     -- ILoString
     ... this.listLowerCase() ...     -- ILoString
     ... this.lowerCase(String word) ...     -- String
     ... this.sort() ...     -- ILoString
     ... this.sortHelper() ...     -- ILoString
     ... this.isSorted() ...     -- boolean
     ... this.isSortedHelper(String curr) ...     -- boolean
     ... this.interleave(ILoString list) ...     -- ILoString
     ... this.merge(ILoString list) ...     -- ILoString
     ... this.rerverse() ...     -- ILoString
     ... this.reverseHelper(ILoString listOld) ...     -- ILoString
     ... this.reverseLast() ...     -- String
     ... this.removeLast() ...     -- ILoString
     ... this.isDoubledList() ...     -- boolean
     ... this.isDoubledListHelper(String word, boolean doCompare) ...     -- boolean
     ... this.isPalindromeList() ... -- boolean
     ... this.countList(int curr) ... -- int
     ... this.firstX(int curr) ... -- ILoString


     METHODS FOR FIELDS
     ... this.first.concat(String) ...        -- String
     ... this.first.compareTo(String) ...     -- int
     ... this.rest.combine() ...              -- String
     ... this.curr.compareTo(String) ... -- String
     ... this.rest.smallest(String) ... -- String
     ... this.small.equals(String) ... -- String
     ... this.rest.removeSmallest(String) ... -- String
     ... this.rest.removeSmallest(String) ... -- String
     ... this.rest.listLowerCase() ... -- ILoString
     ... this.curr.compareTo(this.first) ... -- ILoString
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }  

  //// sort
  // finds the smallest string value in a list
  public String smallest(String curr) {
    if (curr.compareTo(this.first) > 0) {
      return this.rest.smallest(this.first);
    }
    else {
      return this.rest.smallest(curr);
    }
  }

  // it puts the smallest valued string in a list first
  public ILoString removeSmallest(String small) {
    if (small.equals(this.first)) {
      return this.rest;
    }
    else {
      return new ConsLoString(this.first, this.rest.removeSmallest(small));
    }
  }

  // actually goes through the entire list and lowercases it
  public ILoString listLowerCase() {
    return new ConsLoString(lowerCase(this.first), this.rest.listLowerCase());
  }

  // lowercases individual words
  public String lowerCase(String word) {
    return word.toLowerCase();
  }  

  // lowercases the words in a list and sorts them
  public ILoString sort() {
    return this.listLowerCase().sortHelper();
  }

  // sorts a list based on values are the smallest
  public ILoString sortHelper() {
    String small = this.smallest(this.first);
    if (small.equals(this.first)) {
      return new ConsLoString(this.first, this.rest.sortHelper());
    }
    else {
      return new ConsLoString(small, this.removeSmallest(small).sortHelper());
    }
  }

  /////// is sorted
  // checks to see if the list is actually sorted
  public boolean isSorted() {
    return this.listLowerCase().isSortedHelper(this.first.toLowerCase());
  }

  // 
  public boolean isSortedHelper(String curr) {
    if (curr.compareTo(this.first) <= 0) {
      return this.rest.isSortedHelper(this.first);
    }
    else {
      return false;
    }
  }

  ///// interleave code 
  // takes a given list and new list and produces a new list where the first, third, fifth... 
  // elements are from new list, and the second, fourth, sixth... elements are from the given list
  public ILoString interleave(ILoString list) {
    return new ConsLoString(this.first, list.interleave(this.rest));
  }

  ///// merge code
  // merges this list and that list and sorts them both
  public ILoString merge(ILoString list) {
    return list.compareMerge(this.first, this.rest);
  }

  // merges this list and that list and produces a new that is sorted
  public ILoString compareMerge(String that, ILoString list) {
    if (that.compareTo(this.first) > 0) {
      ILoString newList = new ConsLoString(that, list);
      return new ConsLoString(this.first, newList.merge(this.rest));
    }
    else {
      return new ConsLoString(that, list.merge(this));
    }
  }

  // reverses the order of the list
  public ILoString reverse() {
    return this.reverseHelper(this);
  }

  // when the old list gets to MtLo, it returns the old list, or it continues to go through the old
  // list reversing it
  public ILoString reverseHelper(ILoString listOld) {
    if (listOld.reverseLast().equals("")) {
      return listOld;
    } else {
      return new ConsLoString(listOld.reverseLast(), reverseHelper(listOld.removeLast()));
    }
  }
  
  // helps to find the last element in the list and place it first
  public String reverseLast() {
    if (this.rest.reverseLast().equals("")) {
      return this.first;
    }
    else {
      return this.rest.reverseLast();
    }
  }
  
  // helps to remove the last item in the list 
  public ILoString removeLast() {
    if (this.rest.reverseLast().equals("")) {
      return this.rest;
    }
    else {
      return new ConsLoString(this.first, this.rest.removeLast());
    }
  }
  
  //  checks to see if the list is purely doubles ("a" "a" "b" "b")
  public boolean isDoubledList() {
    return this.rest.isDoubledListHelper(this.first, true);
  }

  // returns a boolean based on seeing if the list contains doubles by grouping up elements
  // in the list and comparing them
  public boolean isDoubledListHelper(String word, boolean doCompare) {
    if (doCompare) {
      if (this.first.compareTo(word) == 0) {
        return this.rest.isDoubledListHelper(word, !doCompare);
      } else {
        return !doCompare;
      }
    } else {
      return this.rest.isDoubledListHelper(this.first, !doCompare);
    }
  }
  
  // palindrome
  //  finds the amount of items in the list, takes the first half of the list (x), reverses the
  //  second half of the list, interleaves them, then sees if it is a doubles list by comparing the
  //  two halves to see if it can be read the same way forwards as backwards
  public boolean isPalindromeList() {
    int count = this.countList(0);   
    
    if (count % 2 != 0) {
      count--;
    } 
    
    count = count / 2;
    
    ILoString firstList = this.firstX(count);
    ILoString secondList = this.reverse().firstX(count);
    
    return firstList.interleave(secondList).isDoubledList();
    
  }
  
  // counts the items in a list
  public int countList(int curr) {
    return this.rest.countList(curr + 1);

  }
  
  // finds the first (x) variables in a list
  public ILoString firstX(int curr) {
    if (curr == 0) {
      return new MtLoString();
    } else {
      return new ConsLoString(this.first, this.rest.firstX(curr - 1));
    }
  }
}


// to represent examples for lists of strings
class ExamplesStrings {

  ILoString mary = new ConsLoString("Mary ",
      new ConsLoString("had ",
          new ConsLoString("a ",
              new ConsLoString("little ",
                  new ConsLoString("lamb.", new MtLoString())))));

  ILoString marySorted =  
      new ConsLoString("a ",
          new ConsLoString("had ",
              new ConsLoString("lamb.",
                  new ConsLoString("little ",
                      new ConsLoString("mary " , 
                          new MtLoString())))));


  ILoString fruit = new ConsLoString("apple",
      new ConsLoString("banana",
          new ConsLoString("orange",
              new MtLoString())));

  ILoString doubles = new ConsLoString("a",
      new ConsLoString("a",
          new ConsLoString("b",
              new ConsLoString("b",
                  new MtLoString()))));

  ILoString aMt = new ConsLoString("a", new MtLoString());
  
  ILoString palindrome = new ConsLoString("a",
      new ConsLoString("b",
          new ConsLoString("b",
              new ConsLoString("a",
                  new MtLoString()))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return 
        t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
  }

  // Sort
  boolean testSort1(Tester t) {
    return t.checkExpect(this.mary.sort(), 
        new ConsLoString("a ",
            new ConsLoString("had ",
                new ConsLoString("lamb.",
                    new ConsLoString("little ",
                        new ConsLoString("mary " , 
                            new MtLoString()))))));
  }

  boolean testSort2(Tester t) {
    return t.checkExpect(this.fruit.sort(), this.fruit); 

  }


  // isSorted
  boolean testIsSorted1(Tester t) {
    return t.checkExpect(this.mary.isSorted(), false);
  }

  boolean testIsSorted2(Tester t) {
    return t.checkExpect(this.fruit.isSorted(), true);
  }

  // interleave
  boolean testInterleave1(Tester t) {
    return t.checkExpect(this.mary.interleave(fruit), 
        new ConsLoString("Mary ",
            new ConsLoString("apple",
                new ConsLoString("had ",
                    new ConsLoString("banana",
                        new ConsLoString("a ",
                            new ConsLoString("orange",
                                new ConsLoString("little ",
                                    new ConsLoString("lamb.",
                                        new MtLoString())))))))));

  }

  boolean testInterleave2(Tester t) {
    return t.checkExpect(this.fruit.interleave(mary), 
        new ConsLoString("apple",
            new ConsLoString("Mary ",
                new ConsLoString("banana",
                    new ConsLoString("had ",
                        new ConsLoString("orange",
                            new ConsLoString("a ",
                                new ConsLoString("little ",
                                    new ConsLoString("lamb.",
                                        new MtLoString())))))))));

  }

  /// merge
  boolean testMerge1(Tester t) {
    return t.checkExpect(this.fruit.merge(marySorted), 
        new ConsLoString("a ",
            new ConsLoString("apple",
                new ConsLoString("banana",
                    new ConsLoString("had ",
                        new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                new ConsLoString("mary ",
                                    new ConsLoString("orange",
                                        new MtLoString())))))))));

  }
  
  boolean testMerge2(Tester t) {
    return t.checkExpect(this.fruit.merge(new MtLoString()), fruit);     
  }

  // reverse tests
  boolean testReverse1(Tester t) {
    return t.checkExpect(this.mary.reverse(), new ConsLoString("lamb.",
        new ConsLoString("little ",
            new ConsLoString("a ",
                new ConsLoString("had ",
                    new ConsLoString("Mary ",
                        new MtLoString()))))));
  }
  
  boolean testReverse2(Tester t) {
    return t.checkExpect(this.fruit.reverse(), new ConsLoString("orange",
        new ConsLoString("banana",
            new ConsLoString("apple",
                new MtLoString()))));
  }

  ILoString mt = new MtLoString();
  
  // is doubled list test
  boolean testIsDoubledList1(Tester t) {
    return t.checkExpect(this.doubles.isDoubledList(), true);
  }

  boolean testIsDoubledList2(Tester t) {
    return t.checkExpect(this.aMt.isDoubledList(), false);
  }
  
  // is palindrome list tests
  boolean testIsPalindromeList1(Tester t) {
    return t.checkExpect(this.palindrome.isPalindromeList(), true);
  }
  
  boolean testIsPalindromeList2(Tester t) {
    return t.checkExpect(this.fruit.isPalindromeList(), false);
  }
  
  boolean testIsPalindromeList3(Tester t) {
    return t.checkExpect(this.mt.isPalindromeList(), true);
  }
  
  boolean testCountList(Tester t) {
    return t.checkExpect(this.palindrome.countList(0), 4);
  }

}  


