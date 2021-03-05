from nltk import sent_tokenize
from random import shuffle
import re

doc_file = open("./BAWE/combined/combined.txt", "r", encoding="utf8")

sentences = []

regex = re.compile(r"\<(.*?)\>")

for line in doc_file.readlines():
    curr_sentences = sent_tokenize(line)
    for sent in curr_sentences:
        stripped = regex.sub("", sent).strip()
        if len(stripped) == 0:
            continue
        sentences.append(stripped)

shuffle(sentences)

outfile = open("./BAWE/sentences/sentences.txt", "w+", encoding="utf8")

for sentence in sentences:
    outfile.write(f"{sentence}\n")
