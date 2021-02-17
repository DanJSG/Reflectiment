# Accuracy of random classifier:
#   The probability of an event occurring squared summed across all possibilities eg.
#       P(pred=0) = P(class=0) = 0.5
#       P(pred=1) = P(class=1) = 0.5
#       accuracy = (0.5 * 0.5) + (0.5 * 0.5)
#       accuracy = 0.5
# Can be used to compare model accuracies to random baselines with different numbers of classes and entities in the datasets.

def dataset_random_accuracy(filepath, num_categories):
    file = open(filepath)
    counts = [0 for _ in range(0, num_categories)]
    total = 0
    for line in file.readlines():
        counts[int(line)] += 1
        total += 1
    probabilities = [count/total for count in counts]
    acc = 0
    for prob in probabilities:
        acc += prob * prob
    return acc

filepath = "./processed_datasets/tri_train_y.txt"
num_categories = 5

if __name__ == '__main__':
    print(dataset_random_accuracy(filepath, num_categories))

