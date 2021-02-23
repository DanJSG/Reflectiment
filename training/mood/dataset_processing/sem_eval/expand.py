import copy
import re
import string
from typing import IO
from nltk import word_tokenize
from nltk.corpus import wordnet as wn
import nltk

def get_class(score):
    return 0 if score < 0.5 else 1

def remove_emoji(string):
    emoji_pattern = re.compile("["
                               u"\U0001F600-\U0001F64F"  # emoticons
                               u"\U0001F300-\U0001F5FF"  # symbols & pictographs
                               u"\U0001F680-\U0001F6FF"  # transport & map symbols
                               u"\U0001F1E0-\U0001F1FF"  # flags (iOS)
                               u"\U00002500-\U00002BEF"  # chinese char
                               u"\U00002702-\U000027B0"
                               u"\U00002702-\U000027B0"
                               u"\U000024C2-\U0001F251"
                               u"\U0001f926-\U0001f937"
                               u"\U00010000-\U0010ffff"
                               u"\u2640-\u2642"
                               u"\u2600-\u2B55"
                               u"\u200d"
                               u"\u23cf"
                               u"\u23e9"
                               u"\u231a"
                               u"\ufe0f"  # dingbats
                               u"\u3030"
                               "]+", flags=re.UNICODE)
    return emoji_pattern.sub(r'', string)

def update_score(dict, sentence):
    if sentence in sentence_dict:
        if emotion == 'joy':
            dict[sentence][0] = score
        elif emotion == 'anger':
            dict[sentence][1] = score
        elif emotion == 'fear':
            dict[sentence][2] = score
        else:
            dict[sentence][3] = score
    else:
        if emotion == 'joy':
            dict[sentence] = [score, 0, 0, 0]
        elif emotion == 'anger':
            dict[sentence] = [0, score, 0, 0]
        elif emotion == 'fear':
            dict[sentence] = [0, 0, score, 0]
        else:
            dict[sentence] = [0, 0, 0, score]
    return dict

def get_emotion_scores(emotion, scores):
    if emotion == 'joy':
        return scores[0]
    elif emotion == 'anger':
        return scores[1]
    elif emotion == 'fear':
        return scores[2]
    else:
        return scores[3]

#             0       1       2         3
emotions = ['joy', 'anger', 'fear', 'sadness']
sentence_dict = {}
emotion_files = {}

for emotion in emotions:
    file = open(f"./dataset_processing/sem_eval/{emotion}.train.txt", "r", encoding='utf8')
    for line in file.readlines():
        sections = line.split("\t")
        sentence = remove_emoji(sections[1])
        sentence = sentence.translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()      
        sentence = " ".join(word_tokenize(sentence))
        score = float(sections[3].strip("\n"))
        sentence_dict = update_score(sentence_dict, sentence)
    emotion_files[emotion] = open(f"./processed_datasets/sem_eval/y.{emotion}.train.txt", "w+")

x_file = open("./processed_datasets/sem_eval/x.train.txt", "w+", encoding='utf8')

sentences = []
combined_scores = []
for key in sentence_dict.keys():
    print(key)
    # x_file.write(f"{key}\n")
    sentences.append(key)
    scores = []
    for emotion in emotions:
        # scores.append(get_class(get_emotion_scores(emotion, sentence_dict[key])))
        scores.append(get_emotion_scores(emotion, sentence_dict[key]))
    combined_scores.append(scores)

# print(len(sentences))
# print(len(combined_scores))

data_x = []
data_y = []

progress = 0
entries_count = 0
for sentence, scores in zip(sentences, combined_scores):
    if progress % 25 == 0:
        print(entries_count)
    data_x.append(sentence)
    data_y.append(scores)
    tokenized = word_tokenize(sentence)
    tagged = nltk.pos_tag(tokenized)
    word_index = 0
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
                data_x.append(alt_sentence)
                data_y.append(scores)
        word_index += 1
    entries_count = len(data_x)
    progress += 1

x_outfile = open("./processed_datasets/sem_eval/correlation/x.combined.train.txt", "w+", encoding='utf8')
y_outfile = open("./processed_datasets/sem_eval/correlation/y.combined.train.txt", "w+")

for x, y in zip(data_x, data_y):
    x_outfile.write(f"{x}\n")
    y_str = [str(item) for item in y]
    score_str = ",".join(y_str)
    y_outfile.write(f"{score_str}\n")
    # print(score_str)

print("Complete.")