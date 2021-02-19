## This script splits the Sentiment Treebank Dataset into 3 parts: a training set, a test set and a validation set. 
## The split is done such that 70% of the data goes into the training set, 15%  into the validation set and another 15% into the test set.
import random

# Open original dataset files
scores_filepath = "./dataset_processing/sentiment_treebank/scores.dataset.txt"
sentences_filepath = "./dataset_processing/sentiment_treebank/sentences.dataset.txt"
scores_file = open(scores_filepath, "r")
sentences_file = open(sentences_filepath, "r")

# Create training set files and open in a writeable format
train_x_filepath = "./dataset_processing/sentiment_treebank/sentences.train.dataset.txt"
train_y_filepath = "./dataset_processing/sentiment_treebank/scores.train.dataset.txt"
train_x_file = open(train_x_filepath, "w+")
train_y_file = open(train_y_filepath, "w+")

# Create validation set files and open in a writeable format
validation_x_filepath = "./dataset_processing/sentiment_treebank/sentences.validation.dataset.txt"
validation_y_filepath = "./dataset_processing/sentiment_treebank/scores.validation.dataset.txt"
validation_x_file = open(validation_x_filepath, "w+")
validation_y_file = open(validation_y_filepath, "w+")

# Create test set files and open in a writeable format
test_x_filepath = "./dataset_processing/sentiment_treebank/sentences.test.dataset.txt"
test_y_filepath = "./dataset_processing/sentiment_treebank/scores.test.dataset.txt"
test_x_file = open(test_x_filepath, "w+")
test_y_file = open(test_y_filepath, "w+")

# Count the number of lines in the original dataset
n_lines = len(open(sentences_filepath, "r").readlines())

# Shuffle the order of the lines so that the dataset is split randomly
# Sentences and scores are zipped before shuffling so that they remain correctly ordered
score_lines = scores_file.readlines()
sentences_lines = sentences_file.readlines()
zipped = list(zip(score_lines, sentences_lines))
random.shuffle(zipped)
score_lines, sentences_lines = zip(*zipped)

print(n_lines)
# Loop through the sentences and scores and distribute accordingly between the three files
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
