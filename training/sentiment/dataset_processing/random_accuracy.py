def dataset_random_accuracy(filepath, num_categories):
    file = open(filepath)
    counts = [0 for _ in range(0, num_categories)]
    total = 0
    for line in file.readlines():
        counts[int(line)] += 1
        total += 1
    probabilities = [count/total for count in counts]
    print(probabilities)
    acc = 0
    for prob in probabilities:
        acc += prob * prob
    return acc, probabilities

if __name__ == '__main__':
    filepath = "./processed_datasets/sentiment_treebank_ext/binary/tri_validation_y.txt"
    num_categories = 2
    accuracy, probabilities = dataset_random_accuracy(filepath, num_categories)
    print(f"Random accuracy: {accuracy}")
    print(f"Dataset split: {probabilities}")

