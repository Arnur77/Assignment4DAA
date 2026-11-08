package suffix;

public class Kasai {
    // suffixArray[i] is the starting index of the i-th suffix in sorted order.
    // lcp[i] = LCP(suffixArray[i], suffixArray[i - 1]); lcp[0] = 0.
    public static int[] buildLcp(String text, int[] suffixArray) {
        int n = text.length();
        if (suffixArray.length != n) throw new IllegalArgumentException("Suffix array length must equal text length");
        boolean[] seen = new boolean[n];
        for (int index : suffixArray) {
            if (index < 0 || index >= n || seen[index]) {
                throw new IllegalArgumentException("Invalid suffix array");
            }
            seen[index] = true;
        }

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[suffixArray[i]] = i;
        int[] lcp = new int[n];
        int common = 0;

        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) {
                common = 0;
                continue;
            }
            int j = suffixArray[r - 1];
            while (i + common < n && j + common < n
                    && text.charAt(i + common) == text.charAt(j + common)) {
                common++;
            }
            lcp[r] = common;
            if (common > 0) common--;
        }
        return lcp;
    }
}
