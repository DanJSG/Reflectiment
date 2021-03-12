## Script for testing the accuracy of the saved model against the set of test data.

import json
import tensorflow as tf
from keras.preprocessing.sequence import pad_sequences
from nltk.tokenize import word_tokenize
from keras import backend as K

def r(y_true, y_pred):
    true_mean = K.mean(y_true)
    pred_mean = K.mean(y_pred)
    top = K.sum((y_true - true_mean) * (y_pred - pred_mean))
    bottom = K.sqrt(K.sum(K.pow(y_true - true_mean, 2) * K.sum(K.pow(y_pred - pred_mean, 2))))
    return K.mean(top / bottom)

def load_word_mappings():
    word2index = json.loads(open("word2index.json", "r").read())
    index2word = json.loads(open("index2word.json", "r").read())
    return word2index, index2word

def get_test_data():
    sentences_file = open("./processed_datasets/sem_eval/correlation/x.combined.test.txt", "r", encoding='utf8')
    scores_file = open("./processed_datasets/sem_eval/correlation/y.combined.test.txt")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    split_line = [line.split(",") for line in scores_file.readlines()]
    joint_categories = []
    for arr in split_line:
        new_arr = []
        for val in arr:
            # new_arr.append(int(val.strip("\n")))
            new_arr.append(float(val.strip("\n")))
        joint_categories.append(new_arr)
    return tokenized_sentences, joint_categories

def get_validation_data():
    sentences_file = open("./processed_datasets/sem_eval/correlation/x.combined.dev.txt", "r", encoding='utf8')
    scores_file = open("./processed_datasets/sem_eval/correlation/y.combined.dev.txt")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    split_line = [line.split(",") for line in scores_file.readlines()]
    joint_categories = []
    for arr in split_line:
        new_arr = []
        for val in arr:
            # new_arr.append(int(val.strip("\n")))
            new_arr.append(float(val.strip("\n")))
        joint_categories.append(new_arr)
    return tokenized_sentences, joint_categories

def get_word_index(word2index, word):
    try:
        return word2index[word]
    except:
        return 2999999

def index_sentence_words(word2index, sentences):
    indexed_sentences = []
    for sentence in sentences:
        indexed_sentence = []
        for word in sentence:
            indexed_sentence.append(get_word_index(word2index, word))
        indexed_sentences.append(indexed_sentence)
    return indexed_sentences

def preprocess_sentences(word2index, sentences):
    indexed_sentences = index_sentence_words(word2index, sentences)
    max_sentence_len = 52
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return padded_sentences.tolist(), max_sentence_len

word2index, index2word = load_word_mappings()
x_test, y_test = get_test_data()
x_test_padded, _ = preprocess_sentences(word2index, x_test)

x_validation, y_validation = get_validation_data()
x_validation_padded, _ = preprocess_sentences(word2index, x_validation)

model: tf.keras.Model = tf.keras.models.model_from_json(open("./models/20210224-102401/Uni+Bi-LSTM.json", "r").read())
model.load_weights("./models/20210224-102401/Uni+Bi-LSTM.hdf5")
model.compile(loss='mean_squared_error', metrics=[r, 'mean_squared_error'])
model.summary()
test_evaluation = model.evaluate(x_test_padded, y_test)
validation_evaluation = model.evaluate(x_validation_padded, y_validation)
print(model.metrics_names)
print(test_evaluation)
print(validation_evaluation)
