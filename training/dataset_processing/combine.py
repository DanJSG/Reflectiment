import random

def combine_datasets():
    train_x1 = open("./processed_datasets/sentiment_treebank/tri_validation_x.txt", "r").readlines()
    train_y1 = open("./processed_datasets/sentiment_treebank/tri_validation_y.txt", "r").readlines()
    train_x2 = open("./processed_datasets/sentiment140/tri_validation_x.txt", "r").readlines()
    train_y2 = open("./processed_datasets/sentiment140/tri_validation_y.txt", "r").readlines()
    out_train_x = open("./processed_datasets/tri_validation_x.txt", "w+")
    out_train_y = open("./processed_datasets/tri_validation_y.txt", "w+")

    pairs = [[text.strip("\n"), int(label.strip("\n"))] for text, label in zip(train_x1, train_y1)]
    [pairs.append([text.strip("\n"), int(label.strip("\n"))]) for text, label in zip(train_x2, train_y2)]
    random.shuffle(pairs)

    for text, label in pairs:
        out_train_x.write(f"{text}\n")
        out_train_y.write(f"{label}\n")

if __name__ == '__main__':
    combine_datasets()
