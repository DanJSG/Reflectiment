from nltk.corpus import wordnet as wn
import nltk
import copy

file = open("./expansion/to_expand.txt", "r", encoding="utf8")

more_sentences = []

for line in file.readlines():
    tokenized = nltk.word_tokenize(line)
    tagged = nltk.pos_tag(tokenized)
    word_index = 0
    more_sentences.append(line)
    for tag in tagged:
        alternatives = set()
        if tag[1].startswith("N") or tag[1].startswith("V") or tag[1].startswith('J'):
            synonyms = wn.synsets(tag[0])
            for synonym in synonyms:
                if '_' in synonym.lemmas()[0].name():
                    continue
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
                more_sentences.append(alt_sentence)
        word_index += 1

print(len(more_sentences))
