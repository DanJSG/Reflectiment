from statistics import mean

file = open("./dataset_processing/scores.txt", "r", encoding="utf8")
outfile = open("./dataset_processing/single_scores.txt", "w+")

single_scores = []

for line in file.readlines():
    scores = [(int(item) - 1) / 4 for item in line.split(",")]
    single_scores.append(mean(scores))

max = max(single_scores)
min = min(single_scores)

for score in single_scores:
    normalized = (score - min) / (max - min)
    outfile.write(f"{round(normalized, 5)}\n")
