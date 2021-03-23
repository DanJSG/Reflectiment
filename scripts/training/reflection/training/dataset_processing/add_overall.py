# Adds the overall (average) reflection score to the dataset

subsets = ["test", "train"]

for subset in subsets:
    outfile = open(f"./datasets/multi_extended_overall/scores.{subset}.txt", "w+")
    scores = [[float(val) for val in line.strip("\n").split(",")] for line in open(f"./datasets/multi_extended/scores.{subset}.txt", "r").readlines()]
    avgs = [round(sum(vals) / len(vals), 5) for vals in scores]
    new_scores = [[avgs[i]] + scores[i] for i in range(len(scores))]
    for score_line in new_scores:
        str_scores = [str(score) for score in score_line]
        outfile.write(f"{','.join(str_scores)}\n")

