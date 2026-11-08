import dp.LCS;
import dp.EditDistance;
import matching.NaiveSearch;
import matching.KMP;
import matching.RabinKarp;
import suffix.Kasai;
import java.util.*;

public class AlgorithmTests {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void checkMatching(String text, String pattern, List<Integer> expected) {
        List<Integer> a = NaiveSearch.search(text, pattern);
        List<Integer> b = KMP.search(text, pattern);
        List<Integer> c = RabinKarp.search(text, pattern);
        check(a.equals(expected), "Naive: " + text + " / " + pattern + " -> " + a);
        check(b.equals(expected), "KMP: " + text + " / " + pattern + " -> " + b);
        check(c.equals(expected), "Rabin-Karp: " + text + " / " + pattern + " -> " + c);
    }

    public static void main(String[] args) {
        check(LCS.length("abc", "abc") == 3, "LCS identical");
        check(LCS.length("abc", "xyz") == 0, "LCS different");
        check(LCS.length("", "abc") == 0, "LCS empty");
        check(EditDistance.distance("same", "same") == 0, "Edit identical");
        check(EditDistance.distance("", "abc") == 3, "Edit empty");
        check(EditDistance.distance("cat", "cut") == 1, "Edit substitution");
        check(EditDistance.distance("cat", "cats") == 1, "Edit insertion");
        check(EditDistance.distance("cats", "cat") == 1, "Edit deletion");

        checkMatching("abcXYZ", "abc", Arrays.asList(0));
        checkMatching("XXabcYY", "abc", Arrays.asList(2));
        checkMatching("XXabc", "abc", Arrays.asList(2));
        checkMatching("aaaaa", "aa", Arrays.asList(0, 1, 2, 3));
        checkMatching("abcdef", "xyz", Collections.emptyList());
        checkMatching("abc", "abcdef", Collections.emptyList());
        checkMatching("abc", "", Arrays.asList(0, 1, 2, 3));

        int[] lcp = Kasai.buildLcp("banana", new int[]{5, 3, 1, 0, 4, 2});
        check(Arrays.equals(lcp, new int[]{0, 1, 3, 0, 0, 2}), "Kasai expected [0, 1, 3, 0, 0, 2]");
        System.out.println("All tests passed.");
    }
}
