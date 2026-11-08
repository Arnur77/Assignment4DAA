import dp.LCS;
import dp.EditDistance;
import matching.NaiveSearch;
import matching.KMP;
import matching.RabinKarp;
import suffix.Kasai;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("LCS(ABCBDAB, BDCABA) = " + LCS.length("ABCBDAB", "BDCABA"));
        System.out.println("EditDistance(kitten, sitting) = " + EditDistance.distance("kitten", "sitting"));

        String text = "ababcabcab";
        String pattern = "abc";
        System.out.println("Naive matches = " + NaiveSearch.search(text, pattern));
        System.out.println("KMP matches = " + KMP.search(text, pattern));
        System.out.println("Rabin-Karp matches = " + RabinKarp.search(text, pattern));

        String sample = "banana";
        int[] suffixArray = {5, 3, 1, 0, 4, 2}; // $, a, ana, banana, na, nana
        System.out.println("Kasai LCP = " + Arrays.toString(Kasai.buildLcp(sample, suffixArray)));
    }
}
