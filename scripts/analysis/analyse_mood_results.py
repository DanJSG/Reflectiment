import statistics as stats
from scipy.stats import pearsonr

def five_category(score):
    if score >= 0 and score < 0.2:
        return 0
    elif score >= 0.2 and score < 0.4:
        return 1
    elif score >= 0.4 and score < 0.6:
        return 2
    elif score >= 0.6 and score < 0.8:
        return 3
    elif score >= 0.8 and score <= 1:
        return 4

def three_category(score):
    if score >= 0 and score < 0.33:
        return 0
    elif score >= 0.33 and score < 0.66:
        return 1
    elif score >= 0.66 and score <= 1:
        return 2

def two_category(score):
    return 0 if score < 0.5 else 1

def write_basic_stats(outfile, errors):
    outfile.write(f"Mean Squared Error: {stats.mean(errors)}\n")
    outfile.write(f"Variance: {stats.variance(errors)}\n")
    outfile.write(f"Standard Deviation: {stats.stdev(errors)}\n")
    outfile.write(f"Maximum Error: {max(errors)}\n")
    outfile.write(f"Minimum Error: {min(errors)}\n")

def categorical_accuracy(outfile, actual_scores, gold_scores, category_fn, label):
    total = len(actual_scores)
    correct = 0
    for i in range(len(actual_scores)):
        if category_fn(actual_scores[i]) == category_fn(gold_scores[i]):
            correct += 1
    outfile.write(f"{label} accuracy: {(correct / total) * 100}%\n")

def pearson_r(outfile, actual_scores, gold_scores):
    r = pearsonr(actual_scores, gold_scores)[0]
    outfile.write(f"Pearson correlation coefficient (r): {r}\n")

def main():
    analysis_types = ["lexical", "ml", "averaged"]
    moods = ["joy", "anger", "fear", "sadness"]
    n_items = len(open(f"./results/mood/{moods[0]}/squared_error_{analysis_types[0]}.txt", "r", encoding="utf8").readlines())
    for mood in moods:
        outfile = open(f"./analysed_results/mood/analysed_mood_{mood}.txt", "w+")
        for i in range(len(analysis_types)):
            errors = [float(line.split(",")[1].strip("\n")) for line in open(f"./results/mood/{mood}/squared_error_{analysis_types[i]}.txt", "r", encoding="utf8").readlines() if "#" not in line]
            actual_scores = [float(line.split(",")[1].strip("\n")) for line in open(f"./results/mood/{mood}/scores_{analysis_types[i]}.txt", "r", encoding="utf8").readlines() if "#" not in line]
            gold_scores = [float(line.split(",")[2].strip("\n")) for line in open(f"./results/mood/{mood}/scores_{analysis_types[i]}.txt", "r", encoding="utf8").readlines() if "#" not in line]
            outfile.write(f"Total Samples: {n_items}\n")
            outfile.write(f"===================== {analysis_types[i].upper()} ANALYSIS =====================\n")
            pearson_r(outfile, actual_scores, gold_scores)
            write_basic_stats(outfile, errors)
            categorical_accuracy(outfile, actual_scores, gold_scores, two_category, "Binary")
            categorical_accuracy(outfile, actual_scores, gold_scores, three_category, "Three category")
            categorical_accuracy(outfile, actual_scores, gold_scores, five_category, "Five category")

if __name__ == '__main__':
    main()
