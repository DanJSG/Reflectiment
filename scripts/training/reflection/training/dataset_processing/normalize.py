train_file = open("./datasets/extended/scores.train.txt", "r").readlines()
test_file = open("./datasets/extended/scores.test.txt", "r").readlines()

train_outfile = open("./datasets/extended/scores.train.txt", "w+")
test_outfile = open("./datasets/extended/scores.test.txt", "w+")

train_scores = [float(score.strip("\n")) for score in train_file]
test_scores = [float(score.strip("\n")) for score in test_file]

max_min_finder = train_scores + test_scores

max = max(max_min_finder)
min = min(max_min_finder)

train_scores = [(score - min) / (max - min) for score in train_scores]
test_scores = [(score - min) / (max - min) for score in test_scores]

for val in train_scores:
    train_outfile.write(f"{round(val, 5)}\n")

for val in test_scores:
    test_outfile.write(f"{round(val, 5)}\n")

train_outfile.close()
test_outfile.close()
