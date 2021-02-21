## Script which processes the Stanford Sentiment Treebank datasets into formats which can be used to train
## a Keras model. This format is two files, with one containing the sentences converted to lower case with
## all punctuation removed, and the other containing the category labels (one integer per line).

import string, statistics, re, nltk

def get_binary_category(score):
    """Get an integer binary classification label from a score between 0 and 1."""
    if score < 0.5:
        return 0
    else:
        return 1

def get_fine_grained_category(score):
    """Get a 5 class integer classification label from a score between 0 and 1."""
    if score >= 0 and score < 0.2:
        return 0
    elif score >= 0.2 and score < 0.4:
        return 1
    elif score >= 0.4 and score < 0.6:
        return 2
    elif score >= 0.6 and score < 0.8:
        return 3
    else:
        return 4

def get_tri_category(score):
    """Get a 3 class integer classification label from a score between 0 and 1."""
    if score >= 0 and score < 0.3333333333:
        return 0
    elif score >= 0.3333333333 and score < 0.6666666666:
        return 1
    else:
        return 2

def process_to_file(in_sentences_file, in_scores_file, out_sentences_file, out_category_file, category_getter_fn):
    """Processes a pair of Stanford Sentiment Dataset sentence and score files into a simplified sentence file 
    (no punctuation, all lower case, numbers all set to 0) and a category file with classification category labels.
    The number of classification categories is determined by the function category_getter_fn which is passed in as
    and argument."""
    space_regex = re.compile(r"^.[\ ]*")
    regex: re = re.compile(r"\d+")
    for sentence_line, score_line in zip(in_sentences_file.readlines(), in_scores_file.readlines()):
        scores = [int(score) for score in score_line.split(",")[1:]]
        score = statistics.mean(scores)
        score = (score - 1) / 24
        category = category_getter_fn(score)
        sentence = sentence_line.split(",")[1].translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()
        sentence = regex.sub('0', sentence)
        if space_regex.match(sentence) == None:
            continue
        tokenized = nltk.word_tokenize(sentence)
        sentence = " ".join(tokenized).strip()
        out_sentences_file.write(f"{sentence}\n")
        out_category_file.write(f"{category}\n")

if __name__ == '__main__':
    # Open the dataset and output files
    v_scores_file = open("./dataset_processing/sentiment_treebank/scores.validation.dataset.txt", "r")
    v_sentences_file = open("./dataset_processing/sentiment_treebank/sentences.validation.dataset.txt", "r")
    validation_x_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_validation_x.txt", "w+")
    validation_y_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_validation_y.txt", "w+")
    t_scores_file = open("./dataset_processing/sentiment_treebank/scores.test.dataset.txt", "r")
    t_sentences_file = open("./dataset_processing/sentiment_treebank/sentences.test.dataset.txt", "r")
    test_x_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_test_x.txt", "w+")
    test_y_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_test_y.txt", "w+")
    # Process the validation set and the test set
    process_to_file(v_sentences_file, v_scores_file, validation_x_file, validation_y_file, get_fine_grained_category)
    process_to_file(t_sentences_file, t_scores_file, test_x_file, test_y_file, get_fine_grained_category)
