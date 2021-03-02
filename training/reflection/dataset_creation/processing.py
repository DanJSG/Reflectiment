import string
from nltk import sent_tokenize, word_tokenize

file = open("./answers.txt", "r", encoding='utf8')

sentences_set = set()

for line in file.readlines():
    sentences = sent_tokenize(line)
    for sentence in sentences:
        processed_sent = sentence.translate(str.maketrans('','', string.punctuation)).lower().strip("'")
        processed_sent = " ".join(word_tokenize(processed_sent))
        sentences_set.add(processed_sent)

outfile = open("./sentences.txt", "w+", encoding='utf8')

for sentence in sentences_set:
    outfile.write(f"{sentence}\n")
