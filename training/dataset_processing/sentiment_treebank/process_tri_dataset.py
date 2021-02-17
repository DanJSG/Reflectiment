import string, re, statistics, random
import numpy as np
from nltk import word_tokenize

def get_category(score):
    if score >= 0 and score < 0.3333333333:
        return 0
    elif score >= 0.3333333333 and score < 0.6666666666:
        return 1
    else:
        return 2

def main():

    print("Opening all files...")

    scores_file = open("./scores.dataset.txt", "r")
    sentences_file = open("./sentences.dataset.txt", "r")
    train_x_file = open("./processed_datasets/sentiment_treebank/tri_train_x.txt", "w+")
    train_y_file = open("./processed_datasets/sentiment_treebank/tri_train_y.txt", "w+")
    validation_x_file = open("./processed_datasets/sentiment_treebank/tri_validation_x.txt", "w+")
    validation_y_file = open("./processed_datasets/sentiment_treebank/tri_validation_y.txt", "w+")

    print("Files opened.")

    data_x = []
    data_y = []

    space_regex = re.compile(r"^.[\ ]*")
    regex: re = re.compile(r"\d+")
    n_lines = 239232
    
    print("Parsing sentences and calculating scores...")

    for score_line, sentence_line in zip(scores_file.readlines(), sentences_file.readlines()):
        scores = [int(score) for score in score_line.split(",")[1:]]
        score = statistics.mean(scores)
        score = (score - 1) / 24
        category = get_category(score)
        sentence = sentence_line.split(",")[1].translate(str.maketrans('','', string.punctuation)).lower()
        sentence = regex.sub('00', sentence)
        if space_regex.match(sentence) == None:
            continue
        data_x.append(sentence)
        data_y.append(category)
    
    print("Sentences parsed and scores calculated.")
    print("Shuffling data...")

    zipped = list(zip(data_x, data_y))
    random.shuffle(zipped)
    data_x, data_y = zip(*zipped)
    
    print("Data shuffled.")
    print("Writing output...")

    count = 0
    for x, y in zip(data_x, data_y):
        count += 1
        if count > n_lines * 0.3:
            train_x_file.write(f"{x}")
            train_y_file.write(f"{y}\n")
        else:
            validation_x_file.write(f"{x}")
            validation_y_file.write(f"{y}\n")
    
    print("Outputs written.")

if __name__ == '__main__':
    main()
