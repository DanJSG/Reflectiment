import pprint
import json
import numpy as np
import tensorflow as tf
from keras.models import Sequential
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Conv1D, AveragePooling1D
from keras import regularizers
from keras.preprocessing.sequence import pad_sequences
from nltk.tokenize import word_tokenize

def load_word_mappings():
    word2index = json.loads(open("word2index.json", "r").read())
    index2word = json.loads(open("index2word.json", "r").read())
    return word2index, index2word

def get_test_data():
    sentences_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_test_x.txt", "r")
    categories_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_test_y.txt", "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    categories = [[int(line)] for line in categories_file.readlines()]
    return tokenized_sentences, categories

def get_word_index(word2index, word):
    try:
        return word2index[word]
    except:
        # return random.randrange(0, 3000000)
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

model: tf.keras.Model = tf.keras.models.model_from_json(open("./models/20210219-014937/C-LSTM.json", "r").read())
model.load_weights("./models/20210219-014937/C-LSTM.hdf5")
model.summary()
optimizer = tf.keras.optimizers.Adam(lr=1e-4)
model.compile(loss='binary_crossentropy', optimizer=optimizer, metrics=['accuracy'])
evaluation = model.evaluate(x_test_padded, y_test, verbose=1)
print(evaluation)
