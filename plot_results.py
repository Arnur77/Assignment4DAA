import csv
from collections import defaultdict
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/benchmark.csv", newline="", encoding="utf-8")))

dp = defaultdict(list)
for r in rows:
    if r["algorithm"] in ("LCS", "EditDistance"):
        dp[r["algorithm"]].append((int(r["input_size"]), int(r["average_time_ns"])))
plt.figure()
for name, values in dp.items():
    values.sort()
    plt.plot([x for x, _ in values], [y / 1_000_000 for _, y in values], marker="o", label=name)
plt.xlabel("String length")
plt.ylabel("Average execution time (ms)")
plt.title("LCS and Edit Distance")
plt.legend()
plt.grid(True)
plt.savefig("results/dp_plot.png", dpi=160, bbox_inches="tight")
plt.close()

matching = defaultdict(list)
for r in rows:
    if r["algorithm"] in ("Naive", "KMP", "RabinKarp"):
        matching[(r["algorithm"], r["case"])].append((int(r["input_size"]), int(r["average_time_ns"])))
plt.figure()
for (name, case), values in matching.items():
    values.sort()
    plt.plot([x for x, _ in values], [y / 1_000_000 for _, y in values],
             marker="o", label=f"{name} ({case})")
plt.xlabel("Text length")
plt.ylabel("Average execution time (ms)")
plt.title("String matching algorithms")
plt.legend()
plt.grid(True)
plt.savefig("results/matching_plot.png", dpi=160, bbox_inches="tight")
plt.close()
print("Plots saved in results/")
