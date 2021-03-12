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
    file = open(f"./dataset_processing/sem_eval/{emotion}.test.txt", "r", encoding='utf8')
    for line in file.readlines():
        sections = line.split("\t")
        sentence = remove_emoji(sections[1])
        sentence = sentence.translate(str.maketrans('','', string.punctuation)).lower().strip("\n").strip()      
        sentence = " ".join(word_tokenize(sentence))
        score = float(sections[3].strip("\n"))
        sentence_dict = update_score(sentence_dict, sentence)

x_file = open("./processed_datasets/sem_eval/correlation/x.combined.test.txt", "w+", encoding='utf8')
y_file = open("./processed_datasets/sem_eval/correlation/y.combined.test.txt", "w+")

sentences = []
combined_scores = []
for key in sentence_dict.keys():
    # print(key)
    # x_file.write(f"{key}\n")
    sentences.append(key)
    scores = []
    for emotion in emotions:
        # scores.append(get_class(get_emotion_scores(emotion, sentence_dict[key])))
        scores.append(get_emotion_scores(emotion, sentence_dict[key]))
        # file: IO = emotion_files[emotion]
        # file.write(f"{get_class(get_emotion_scores(emotion, sentence_dict[key]))}\n")
    combined_scores.append(scores)
    # print(scores)

print(combined_scores)

for sentence, scores in zip(sentences, combined_scores):
    scores_str_arr = [str(val) for val in scores]
    scores_str = ",".join(scores_str_arr)
    x_file.write(f"{sentence}\n")
    y_file.write(f"{scores_str}\n")
