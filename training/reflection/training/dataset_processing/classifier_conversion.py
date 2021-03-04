def classify_score(score):
    return 1 if score > 0.5 else 0
    # if score >= 0 and score < 0.2:
    #     return 0
    # elif score >= 0.2 and score < 0.4:
    #     return 1
    # elif score >= 0.4 and score < 0.6:
    #     return 2
    # elif score >= 0.6 and score < 0.8:
    #     return 3
    # else:
    #     return 4

train_file = open("./datasets/extended/scores.train.txt", "r").readlines()
test_file = open("./datasets/extended/scores.test.txt", "r").readlines()

train_outfile = open("./datasets/extended/scores.class.train.txt", "w+")
test_outfile = open("./datasets/extended/scores.class.test.txt", "w+")

train_scores = [float(score.strip("\n")) for score in train_file]
test_scores = [float(score.strip("\n")) for score in test_file]

max_min_finder = train_scores + test_scores

max = max(max_min_finder)
min = min(max_min_finder)

train_scores = [(score - min) / (max - min) for score in train_scores]
test_scores = [(score - min) / (max - min) for score in test_scores]

count = 0
for score in train_scores:
    if score > 0.5:
        count += 1

print(count)
print(len(train_file) - count)

train_classifications = [classify_score(score) for score in train_scores]
test_classifications = [classify_score(score) for score in test_scores]

print(len(train_classifications))
print(len(test_classifications))

for val in train_classifications:
    train_outfile.write(f"{val}\n")

for val in test_classifications:
    test_outfile.write(f"{val}\n")

test_outfile.close()
train_outfile.close()