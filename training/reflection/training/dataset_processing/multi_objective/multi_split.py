from math import floor
from random import shuffle
from nltk import word_tokenize
import string

bawe_sentences = open("./dataset_processing/bawe/sentences.txt", "r").readlines()
bawe_scores = open("./dataset_processing/bawe/multi_scores.txt", "r").readlines()

york_sentences = open("./dataset_processing/york/sentences.txt", "r").readlines()
york_scores = open("./dataset_processing/york/multi_scores.txt", "r").readlines()

total_samples = len(bawe_sentences) + len(york_sentences)

n_test_samples = floor(total_samples * 0.3)
n_train_samples = total_samples - n_test_samples

print(total_samples)
print(n_test_samples)
print(n_train_samples)

test_sentences_file = open("./dataset_processing/combined/multi_objective/sentences.test.txt", "w+")
test_scores_file = open("./dataset_processing/combined/multi_objective/scores.test.txt", "w+")

train_sentences_file = open("./dataset_processing/combined/multi_objective/sentences.train.txt", "w+")
train_scores_file = open("./dataset_processing/combined/multi_objective/scores.train.txt", "w+")

sent_scores = list(zip(york_sentences, york_scores))
shuffle(sent_scores)
york_sentences, york_scores = zip(*sent_scores)

count = 0
for sentence, score in zip(york_sentences, york_scores):
    sentence = sentence.strip("\n")
    score = score.strip("\n")
    if count < n_test_samples:
        test_sentences_file.write(f"{sentence}\n")
        test_scores_file.write(f"{score}\n")
    else:
        train_sentences_file.write(f"{sentence}\n")
        train_scores_file.write(f"{score}\n")
    count += 1

for sentence, score in zip(bawe_sentences, bawe_scores):
    sentence = sentence.translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()
    sentence = " ".join(word_tokenize(sentence))
    score = score.strip("\n")
    train_sentences_file.write(f"{sentence}\n")
    train_scores_file.write(f"{score}\n")
