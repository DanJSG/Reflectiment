import random

scores_filepath = "./dataset_processing/sentiment_treebank/scores.dataset.txt"
sentences_filepath = "./dataset_processing/sentiment_treebank/sentences.dataset.txt"
scores_file = open(scores_filepath, "r")
sentences_file = open(sentences_filepath, "r")

train_x_filepath = "./dataset_processing/sentiment_treebank/sentences.train.dataset.txt"
train_y_filepath = "./dataset_processing/sentiment_treebank/scores.train.dataset.txt"

train_x_file = open(train_x_filepath, "w+")
train_y_file = open(train_y_filepath, "w+")

validation_x_filepath = "./dataset_processing/sentiment_treebank/sentences.validation.dataset.txt"
validation_y_filepath = "./dataset_processing/sentiment_treebank/scores.validation.dataset.txt"
validation_x_file = open(validation_x_filepath, "w+")
validation_y_file = open(validation_y_filepath, "w+")

test_x_filepath = "./dataset_processing/sentiment_treebank/sentences.test.dataset.txt"
test_y_filepath = "./dataset_processing/sentiment_treebank/scores.test.dataset.txt"
test_x_file = open(test_x_filepath, "w+")
test_y_file = open(test_y_filepath, "w+")

n_lines = len(open(sentences_filepath, "r").readlines())

score_lines = scores_file.readlines()
sentences_lines = sentences_file.readlines()
zipped = list(zip(score_lines, sentences_lines))
random.shuffle(zipped)
score_lines, sentences_lines = zip(*zipped)

print(n_lines)
count = 0
for sent_line, score_line in zip(sentences_lines, score_lines):
    count += 1
    if count > 0.3 * n_lines:
        train_x_file.write(f"{sent_line}")
        train_y_file.write(f"{score_line}")
    elif count > 0.15 * n_lines:
        validation_x_file.write(f"{sent_line}")
        validation_y_file.write(f"{score_line}")
    else:
        test_x_file.write(f"{sent_line}")
        test_y_file.write(f"{score_line}")
