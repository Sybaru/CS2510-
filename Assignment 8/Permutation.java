import java.util.*;
import tester.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet =
      new ArrayList<Character>(Arrays.asList(
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
      't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Create a new instance of the encoder/decoder with a given random seed
  PermutationCode(int seed) {
    this.rand = new Random(seed);
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> alph = new ArrayList<>(this.alphabet);
    ArrayList<Character> crypt = new ArrayList<Character>();
    for (int i = 0; i < 26; i++) {
      int randChar = this.rand.nextInt(alph.size());
      crypt.add(alph.get(randChar));
      alph.remove(randChar);
    }
    return crypt;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    ArrayList<Character> encoded = this.codeify(this.alphabet, this.code, source);
    return charToString(encoded);
  }

  // produce a decoded String from the given String
  String decode(String code) {
    ArrayList<Character> decoded = this.codeify(this.code, this.alphabet, code);
    return charToString(decoded);
  }

  // abstracts the method of decoding/encoding a string
  ArrayList<Character> codeify(
      ArrayList<Character> cipher, ArrayList<Character> alphas, String source) {
    ArrayList<Character> result = new ArrayList<Character>();
    for (int i = 0; i < source.length(); i++) {
      for (int j = 0; j < 26; j++) {
        if (source.charAt(i) == cipher.get(j)) {
          result.add(alphas.get(j));
        }
      }
    }
    return result;
  }


  // converts an arraylist of chars to a string
  String charToString(ArrayList<Character> chars) {
    StringBuilder build = new StringBuilder(chars.size());
    for (int i = 0; i < chars.size(); i++) {
      build.append(chars.get(i));
    }
    return build.toString();
  }
}

class ExamplesPermutation {
  ArrayList<Character> testCode =
      new ArrayList<Character>(Arrays.asList(
      'z', 'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', //j
      'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', //s
      'g', 'f', 'e', 'd', 'c', 'b', 'a'));

  PermutationCode testerPermutation = new PermutationCode(testCode);

  void testEncode(Tester t) {
    t.checkExpect(testerPermutation.encode("apples"), "zkkovh");
    t.checkExpect(testerPermutation.encode(""), "");
    t.checkExpect(testerPermutation.encode("bruh"), "yifs");
  }

  void testDecode(Tester t) {
    t.checkExpect(testerPermutation.decode("zkkovh"), "apples");
    t.checkExpect(testerPermutation.decode(""), "");
    t.checkExpect(testerPermutation.decode("yifs"), "bruh");
  }

  void testcharToString(Tester t) {
    ArrayList<Character> testString = new ArrayList<Character>(Arrays.asList('a', 'p','p','l','e'));
    ArrayList<Character> emptyString = new ArrayList<Character>(Arrays.asList());
    t.checkExpect(testerPermutation.charToString(testString), "apple");
    t.checkExpect(testerPermutation.charToString(emptyString), "");
  }

  void testInitEncoder(Tester t) {
    PermutationCode random1 = new PermutationCode(2510);
    PermutationCode random2 = new PermutationCode(2510);
    PermutationCode random3 = new PermutationCode(2511);
    t.checkExpect(random1.code, random2.code);
    t.checkExpect(random3.code.equals(random2), false);
  }

  void testCodeify(Tester t) {
    t.checkExpect(testerPermutation.codeify(
        testerPermutation.code, testerPermutation.alphabet, "abc"),
        new ArrayList<Character>(Arrays.asList('z', 'y', 'x')));

    t.checkExpect(testerPermutation.codeify(
        testerPermutation.alphabet, testerPermutation.code, "zyx"),
        new ArrayList<Character>(Arrays.asList('a', 'b', 'c')));

    t.checkExpect(testerPermutation.codeify(
        testerPermutation.alphabet, testerPermutation.code, ""),
        new ArrayList<Character>(Arrays.asList()));
  }



}
