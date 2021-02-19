import string, re, statistics, random, copy
import nltk
from nltk.corpus import wordnet as wn

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

def expand_dataset(sentences_file, scores_file, category_getter_fn):
    """ Expands Stanford Sentiment Treebank dataset file by substituting nouns, verbs and adjectives in each sentence with synonyms
    retrieved from WordNet. Processes into a set of of sentence strings and a set of scores.
    """
    space_regex = re.compile(r"^.[\ ]*")
    regex: re = re.compile(r"\d+")
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
        category = category_getter_fn(score)
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
    return data_x, data_y

def write_data_to_file(x_data, y_data, x_outfile, y_outfile):
    for x, y in zip(x_data, y_data):
        x_outfile.write(f"{x}\n")
        y_outfile.write(f"{y}\n")

if __name__ == '__main__':
    scores_file = open("./dataset_processing/sentiment_treebank/scores.train.dataset.txt", "r")
    sentences_file = open("./dataset_processing/sentiment_treebank/sentences.train.dataset.txt", "r")
    train_x_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_train_x.txt", "w+")
    train_y_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_train_y.txt", "w+")
    data_x, data_y = expand_dataset(sentences_file, scores_file, get_fine_grained_category)
    write_data_to_file(data_x, data_y, train_x_file, train_y_file)
