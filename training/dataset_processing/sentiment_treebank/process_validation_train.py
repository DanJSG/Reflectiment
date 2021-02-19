import string, statistics, re, nltk

v_scores_file = open("./dataset_processing/sentiment_treebank/scores.validation.dataset.txt", "r")
v_sentences_file = open("./dataset_processing/sentiment_treebank/sentences.validation.dataset.txt", "r")
validation_x_file = open("./processed_datasets/sentiment_treebank_ext/binary/five_validation_x.txt", "w+")
validation_y_file = open("./processed_datasets/sentiment_treebank_ext/binary/five_validation_y.txt", "w+")

t_scores_file = open("./dataset_processing/sentiment_treebank/scores.test.dataset.txt", "r")
t_sentences_file = open("./dataset_processing/sentiment_treebank/sentences.test.dataset.txt", "r")
test_x_file = open("./processed_datasets/sentiment_treebank_ext/binary/five_test_x.txt", "w+")
test_y_file = open("./processed_datasets/sentiment_treebank_ext/binary/five_test_y.txt", "w+")

space_regex = re.compile(r"^.[\ ]*")
regex: re = re.compile(r"\d+")

def get_category(score):
    if score < 0.5:
        return 0
    else:
        return 1

for sentence_line, score_line in zip(v_sentences_file.readlines(), v_scores_file.readlines()):
    scores = [int(score) for score in score_line.split(",")[1:]]
    score = statistics.mean(scores)
    score = (score - 1) / 24
    category = get_category(score)
    sentence = sentence_line.split(",")[1].translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()
    sentence = regex.sub('0', sentence)
    if space_regex.match(sentence) == None:
        continue
    tokenized = nltk.word_tokenize(sentence)
    sentence = " ".join(tokenized).strip()
    validation_x_file.write(f"{sentence}\n")
    validation_y_file.write(f"{category}\n")

for sentence_line, score_line in zip(t_sentences_file.readlines(), t_scores_file.readlines()):
    scores = [int(score) for score in score_line.split(",")[1:]]
    score = statistics.mean(scores)
    score = (score - 1) / 24
    category = get_category(score)
    sentence = sentence_line.split(",")[1].translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()
    sentence = regex.sub('0', sentence)
    if space_regex.match(sentence) == None:
        continue
    tokenized = nltk.word_tokenize(sentence)
    sentence = " ".join(tokenized).strip()
    test_x_file.write(f"{sentence}\n")
    test_y_file.write(f"{category}\n")
