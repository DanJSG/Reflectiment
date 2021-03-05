from statistics import mean

file = open("./dataset_processing/york/scores.txt", "r", encoding="utf8")
outfile = open("./dataset_processing/york/single_scores.txt", "w+")
outfile2 = open("./dataset_processing/york/multi_scores.txt", "w+")

single_scores = []

for line in file.readlines():
    scores = [(int(item) - 1) / 4 for item in line.split(",")]
    single_scores.append(mean(scores))
    outfile.write(f"{round(mean(scores), 5)}\n")
    scores_str = ",".join(str(score) for score in scores)
    outfile2.write(f"{scores_str}\n")

# max = max(single_scores)
# min = min(single_scores)

# for score in single_scores:
#     normalized = (score - min) / (max - min)
#     outfile.write(f"{round(normalized, 5)}\n")
