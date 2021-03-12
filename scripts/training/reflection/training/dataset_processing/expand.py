from nltk.corpus import wordnet as wn
import nltk
import copy
import random

file = open("./dataset_processing/combined/sentences.train.txt", "r", encoding="utf8")
scores_file = open("./dataset_processing/combined/scores.train.txt", "r")

more_sentences = []
more_scores = []

for line, score in zip(file.readlines(), scores_file.readlines()):
    tokenized = nltk.word_tokenize(line)
    tagged = nltk.pos_tag(tokenized)
    word_index = 0
    more_sentences.append(line)
    more_scores.append(score.strip("\n"))
    for tag in tagged:
        alternatives = set()
        if tag[1].startswith("N") or tag[1].startswith("V") or tag[1].startswith('J'):
            synonyms = wn.synsets(tag[0])
            for synonym in synonyms:
                if '_' in synonym.lemmas()[0].name():
                    continue
                if synonym.pos() == 'v' and tag[1].startswith("V"):
                    alternatives.add(synonym.lemmas()[0].name().lower())
                elif synonym.pos() == 'n' and tag[1].startswith("N"):
                    alternatives.add(synonym.lemmas()[0].name().lower())
                elif synonym.pos() == 'j' and tag[1].startswith('J'):
                    alternatives.add(synonym.lemmas()[0].name().lower())
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
                more_sentences.append(alt_sentence)
                more_scores.append(score)
        word_index += 1

out_scores_file = open("./datasets/extended/scores.train.txt", "w+")
out_sentences_file = open("./datasets/extended/sentences.train.txt", "w+", encoding="utf8")

expanded_scores = []
expanded_sentences = []

for sentence, score in zip(more_sentences, more_scores):
    sentence = sentence.strip("\n")
    score = score.strip("\n")
    expanded_sentences.append(sentence)
    expanded_scores.append(score)

zipped = list(zip(expanded_sentences, expanded_scores))
random.shuffle(zipped)
shuffled_sentences, shuffled_scores = zip(*zipped)

for sentence, score in zip(shuffled_sentences, shuffled_scores):
    out_sentences_file.write(f"{sentence}\n")
    out_scores_file.write(f"{score}\n")

print(len(more_sentences))
