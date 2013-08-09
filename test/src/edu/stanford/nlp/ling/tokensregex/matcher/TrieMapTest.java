package edu.stanford.nlp.ling.tokensregex.matcher;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test case for TrieMap
 *
 * @author Angel Chang
 */
public class TrieMapTest extends TestCase {
  public void testTrieBasic() throws Exception {
    TrieMap<String,Boolean> trieMap = new TrieMap<String,Boolean>();
    trieMap.put(new String[]{"a","white","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","white","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat","climbed","on","the","sofa"}, Boolean.TRUE);
    System.out.println(trieMap);
    System.out.println(trieMap.toFormattedString());

    // Test get and remove
    assertTrue(trieMap.get(new String[]{"a", "white", "hat"}));
    assertNull(trieMap.get(new String[]{"a", "white"}));
    trieMap.remove(new String[]{"a", "white", "hat"});
    assertTrue(trieMap.get(new String[]{"a", "white", "cat"}));
    assertNull(trieMap.get(new String[]{"a", "white", "hat"}));

    // Test keys
    assertTrue(trieMap.containsKey(new String[]{"a", "white", "cat"}));
    assertFalse(trieMap.containsKey(new String[]{"white", "cat"}));
    assertEquals(3, trieMap.size());
    assertEquals(3, trieMap.keySet().size());
  }

  public void testTrieFindAll() throws Exception {
    TrieMap<String,Boolean> trieMap = new TrieMap<String,Boolean>();
    trieMap.put(new String[]{"a","white","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","white","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat","climbed","on","the","sofa"}, Boolean.TRUE);
    trieMap.put(new String[]{"white"}, Boolean.TRUE);
    TrieMapMatcher<String,Boolean> matcher = new TrieMapMatcher<String, Boolean>(trieMap);
    List<Match<String,Boolean>> matches =
            matcher.findAllMatches(new String[]{"a","white","cat","is","wearing","a","white","hat"});
    List<Match<String,Boolean>> expected = new ArrayList<Match<String,Boolean>>();
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 0, 3));
    expected.add(new Match(Arrays.asList(new String[]{"white"}), Boolean.TRUE, 1, 2));
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8));
    expected.add(new Match(Arrays.asList(new String[]{"white"}), Boolean.TRUE, 6, 7));
    assertEquals("Expecting " + expected.size() + " matches: got " + matches, expected.size(), matches.size());
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
  }

  public void testTrieFindNonOverlapping() throws Exception {
    TrieMap<String,Boolean> trieMap = new TrieMap<String,Boolean>();
    trieMap.put(new String[]{"a","white","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","white","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat","climbed","on","the","sofa"}, Boolean.TRUE);
    trieMap.put(new String[]{"white"}, Boolean.TRUE);
    TrieMapMatcher<String,Boolean> matcher = new TrieMapMatcher<String, Boolean>(trieMap);
    List<Match<String,Boolean>> matches =
            matcher.findNonOverlapping(new String[]{"a","white","cat","is","wearing","a","white","hat","and","a","black","cat","climbed","on","the","sofa"});
    List<Match<String,Boolean>> expected = new ArrayList<Match<String,Boolean>>();
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 0, 3));
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8));
    expected.add(new Match(Arrays.asList(new String[]{"a","black","cat","climbed","on","the","sofa"}), Boolean.TRUE, 9, 16));
    assertEquals("Expecting " + expected.size() + " matches: got " + matches, expected.size(), matches.size());
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
  }

  public void testTrieSegment() throws Exception {
    TrieMap<String,Boolean> trieMap = new TrieMap<String,Boolean>();
    trieMap.put(new String[]{"a","white","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","white","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat","climbed","on","the","sofa"}, Boolean.TRUE);
    trieMap.put(new String[]{"white"}, Boolean.TRUE);
    TrieMapMatcher<String,Boolean> matcher = new TrieMapMatcher<String, Boolean>(trieMap);
    List<Match<String,Boolean>> matches =
            matcher.segment(new String[]{"a","white","cat","is","wearing","a","white","hat","and","a","black","cat","climbed","on","the","sofa"});
    List<Match<String,Boolean>> expected = new ArrayList<Match<String,Boolean>>();
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 0, 3));
    expected.add(new Match(Arrays.asList(new String[]{"is", "wearing"}), null, 3, 5));
    expected.add(new Match(Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8));
    expected.add(new Match(Arrays.asList(new String[]{"and"}), null, 8, 9));
    expected.add(new Match(Arrays.asList(new String[]{"a","black","cat","climbed","on","the","sofa"}), Boolean.TRUE, 9, 16));
    assertEquals("Expecting " + expected.size() + " matches: got " + matches, expected.size(), matches.size());
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
  }

  public void testTrieFindClosest() throws Exception {
    TrieMap<String,Boolean> trieMap = new TrieMap<String,Boolean>();
    trieMap.put(new String[]{"a","white","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","white","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","cat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","black","hat"}, Boolean.TRUE);
    trieMap.put(new String[]{"a","colored","hat"}, Boolean.TRUE);
    TrieMapMatcher<String,Boolean> matcher = new TrieMapMatcher<String, Boolean>(trieMap);
    List<ApproxMatch<String,Boolean>> matches = matcher.findClosestMatches(new String[]{"the", "black", "hat"}, 2);
    List<ApproxMatch<String,Boolean>> expected = new ArrayList<ApproxMatch<String,Boolean>>();
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "hat"}), Boolean.TRUE, 0, 3, 1.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 3, 2.0));
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
    //System.out.println(matches);

    // TODO: ordering of results with same score
    matches = matcher.findClosestMatches(new String[]{"the", "black"}, 5);
    expected = new ArrayList<ApproxMatch<String,Boolean>>();
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 2, 2.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "hat"}), Boolean.TRUE, 0, 2, 2.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 0, 2, 3.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 0, 2, 3.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "colored", "hat"}), Boolean.TRUE, 0, 2, 3.0));
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
    //System.out.println(matches);

    matches = matcher.findClosestMatches(new String[]{"the", "black","cat","is","wearing","a","white","hat"}, 5, false);
    expected = new ArrayList<ApproxMatch<String,Boolean>>();
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 0, 8, 5.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 8, 6.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 0, 8, 6.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "colored", "hat"}), Boolean.TRUE, 0, 8, 6.0));
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "hat"}), Boolean.TRUE, 0, 8, 6.0));
    assertEquals("Expecting " + expected + ", got " + matches, expected, matches);
    //System.out.println(matches);

    //matcher.keepAlignments = true;
    matches = matcher.findClosestMatches(new String[]{"the", "black","cat","is","wearing","a","white","hat"}, 5, true);
    //   [([[a, black, cat]-[a, white, hat]] -> true-true at (0,8),3.0),
    expected = new ArrayList<ApproxMatch<String,Boolean>>();
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat", "a", "white", "hat"}), Boolean.TRUE, 0, 8,
            Arrays.asList(new Match[]{
              new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 3),
              new Match<String,Boolean>( Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8)}),
            3.0));
    // ([[a, black, cat]-[a, colored, hat]] -> true-true at (0,8),4.0),      0000000
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat", "a", "black", "cat", "a", "white", "hat"}), Boolean.TRUE, 0, 8,
            Arrays.asList(new Match[]{
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 3),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 3, 5),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8)}),
            4.0));
    // ([[a, black, hat]-[a, white, hat]] -> true-true at (0,8),4.0),
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat", "a", "white", "cat"}), Boolean.TRUE, 0, 8,
            Arrays.asList(new Match[]{
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 3),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "white", "cat"}), Boolean.TRUE, 5, 8)}),
            4.0));
    // ([[a, black, cat]-[a, black, hat]] -> true-true at (0,8),4.0),
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "hat", "a", "white", "hat"}), Boolean.TRUE, 0, 8,
            Arrays.asList(new Match[]{
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "hat"}), Boolean.TRUE, 0, 5),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8)}),
            4.0));
   // ([[a, black, cat]-[a, white, cat]] -> true-true at (0,8),4.0)]
    expected.add(new ApproxMatch(Arrays.asList(new String[]{"a", "black", "cat", "a", "black", "hat", "a", "white", "hat"}), Boolean.TRUE, 0, 8,
            Arrays.asList(new Match[]{
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "cat"}), Boolean.TRUE, 0, 3),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "black", "hat"}), Boolean.TRUE, 3, 5),
                    new Match<String,Boolean>( Arrays.asList(new String[]{"a", "white", "hat"}), Boolean.TRUE, 5, 8)}),
            4.0));
    assertEquals("Expecting " + expected + ",\ngot " + matches, expected, matches);
  }
}
