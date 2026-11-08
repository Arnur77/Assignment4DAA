package benchmark;

import dp.LCS;
import dp.EditDistance;
import matching.NaiveSearch;
import matching.KMP;
import matching.RabinKarp;
import suffix.Kasai;
import java.util.*;
import java.io.*;

public class Benchmark {
    private static final long SEED = 42L;
    private static final int RUNS = 5;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private static String randomString(int length, Random random) {
        StringBuilder s = new StringBuilder(length);
        for (int i = 0; i < length; i++) s.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        return s.toString();
    }

    private static long averageNanos(Runnable task) {
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            task.run();
            total += System.nanoTime() - start;
        }
        return total / RUNS;
    }

    private static int[] suffixArray(String s) {
        Integer[] boxed = new Integer[s.length()];
        for (int i = 0; i < boxed.length; i++) boxed[i] = i;
        Arrays.sort(boxed, (a, b) -> {
            int i = a, j = b;
            while (i < s.length() && j < s.length()) {
                int cmp = Character.compare(s.charAt(i), s.charAt(j));
                if (cmp != 0) return cmp;
                i++; j++;
            }
            return Integer.compare(s.length() - a, s.length() - b);
        });
        int[] result = new int[boxed.length];
        for (int i = 0; i < boxed.length; i++) result[i] = boxed[i];
        return result;
    }

    public static void main(String[] args) throws Exception {
        File out = new File("results");
        out.mkdirs();
        try (PrintWriter csv = new PrintWriter(new FileWriter("results/benchmark.csv"))) {
            csv.println("algorithm,input_size,average_time_ns,case");
            Random random = new Random(SEED);

            for (int n : new int[]{100, 500, 1000}) {
                String a = randomString(n, random), b = randomString(n, random);
                long lcs = averageNanos(() -> LCS.length(a, b));
                long edit = averageNanos(() -> EditDistance.distance(a, b));
                csv.println("LCS," + n + "," + lcs + ",random");
                csv.println("EditDistance," + n + "," + edit + ",random");
                System.out.println("DP n=" + n + " LCS=" + lcs + " ns EditDistance=" + edit + " ns");
            }

            for (int n : new int[]{10000, 50000, 100000}) {
                String text = randomString(n, random);
                String present = text.substring(Math.max(0, n / 2 - 5), Math.min(n, n / 2 + 5));
                String absent = "~NOTFOUND~";
                for (String[] test : new String[][]{{"present", present}, {"absent", absent}}) {
                    String pattern = test[1];
                    long naive = averageNanos(() -> NaiveSearch.search(text, pattern));
                    long kmp = averageNanos(() -> KMP.search(text, pattern));
                    long rk = averageNanos(() -> RabinKarp.search(text, pattern));
                    csv.println("Naive," + n + "," + naive + "," + test[0]);
                    csv.println("KMP," + n + "," + kmp + "," + test[0]);
                    csv.println("RabinKarp," + n + "," + rk + "," + test[0]);
                    System.out.println("Matching n=" + n + " case=" + test[0] + " Naive=" + naive
                            + " KMP=" + kmp + " RabinKarp=" + rk + " ns");
                }
            }

            for (int n : new int[]{1000, 5000, 10000}) {
                String s = randomString(n, random);
                int[] sa = suffixArray(s);
                long kasai = averageNanos(() -> Kasai.buildLcp(s, sa));
                csv.println("Kasai," + n + "," + kasai + ",random");
                System.out.println("Kasai n=" + n + " time=" + kasai + " ns");
            }
        }
        System.out.println("Results saved to results/benchmark.csv");
    }
}
