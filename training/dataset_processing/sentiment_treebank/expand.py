import string, re, statistics, random, copy
import nltk
from nltk.corpus import wordnet as wn
from googletrans import Translator

def get_category(score):
    if score < 0.5:
        return 0
    else:
        return 1
    # if score >= 0 and score < 0.3333333333:
    #     return 0
    # elif score >= 0.3333333333 and score < 0.6666666666:
    #     return 1
    # else:
    #     return 2

scores_file = open("./dataset_processing/sentiment_treebank/scores.train.dataset.txt", "r")
sentences_file = open("./dataset_processing/sentiment_treebank/sentences.train.dataset.txt", "r")
train_x_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_train_x.txt", "w+")
train_y_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_train_y.txt", "w+")

space_regex = re.compile(r"^.[\ ]*")
regex: re = re.compile(r"\d+")
# n_lines = 239232

data_x = []
data_y = []

entries_count = 0
progress = 0

for score_line, sentence_line in zip(scores_file.readlines(), sentences_file.readlines()):
    if progress % 1000 == 0:
        print(f"Finished processing line {progress}. So far there are {entries_count} lines.")
    scores = [int(score) for score in score_line.split(",")[1:]]
    score = statistics.mean(scores)
    score = (score - 1) / 24
    category = get_category(score)
    sentence = sentence_line.split(",")[1].translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()
    sentence = regex.sub('0', sentence)
    if space_regex.match(sentence) == None:
        progress += 1
        continue
    tokenized = nltk.word_tokenize(sentence)
    tagged = nltk.pos_tag(tokenized)
    data_x.append(" ".join(tokenized))
    data_y.append(category)
    word_index = 0
    for tag in tagged:
        alternatives = set()
        if tag[1].startswith("N") or tag[1].startswith("V") or tag[1].startswith('J'):
            synonyms = wn.synsets(tag[0])
            for synonym in synonyms:
                if synonym.pos() == 'v' and tag[1].startswith("V"):
                    alternatives.add(synonym.lemmas()[0].name())
                elif synonym.pos() == 'n' and tag[1].startswith("N"):
                    alternatives.add(synonym.lemmas()[0].name())
                elif synonym.pos() == 'j' and tag[1].startswith('J'):
                    alternatives.add(synonym.lemmas()[0].name())
        alternative_sentences = set()
        skip_first = 0
        for alternative in alternatives:
            if skip_first == 0:
                skip_first += 1
                continue
            alt_sentence = copy.deepcopy(tokenized)
            alt_sentence[word_index] = alternative
            alternative_sentences.add(" ".join(alt_sentence))
        if len(alternative_sentences) > 0:
            for alt_sentence in alternative_sentences:
                data_x.append(alt_sentence)
                data_y.append(category)
        word_index += 1
    entries_count = len(data_x)
    progress += 1

zipped = list(zip(data_x, data_y))
random.shuffle(zipped)
data_x, data_y = zip(*zipped)

count = 0
for x, y in zip(data_x, data_y):
    count += 1
    train_x_file.write(f"{x}\n")
    train_y_file.write(f"{y}\n")
    # if count > len(data_x) * 0.3:
    #     train_x_file.write(f"{x}\n")
    #     train_y_file.write(f"{y}\n")
    # else:
    #     validation_x_file.write(f"{x}\n")
    #     validation_y_file.write(f"{y}\n")


