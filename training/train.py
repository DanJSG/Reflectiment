import json
import random
import datetime
import tensorflow as tf
import numpy as np
from tensorflow import keras
from keras.models import Sequential
from keras.layers import LSTM, Dropout, Dense, Embedding, Input, BatchNormalization, Conv1D, MaxPool1D, Concatenate, Flatten, AveragePooling1D, Conv2D, AveragePooling2D
from nltk.tokenize import word_tokenize
from keras.preprocessing.sequence import pad_sequences
from keras import regularizers

def load_word_mappings():
    word2index = json.loads(open("word2index.json", "r").read())
    index2word = json.loads(open("index2word.json", "r").read())
    return word2index, index2word

def initialize_gpu():
    physical_devices = tf.config.experimental.list_physical_devices('GPU')
    print("GPUs available: ", len(physical_devices))
    tf.config.experimental.set_memory_growth(physical_devices[0], True)

def Word2Vec(input_length):
    embedding_weights = np.load(open("weights_file.bin", "rb"))
    return Embedding(input_length=input_length, input_dim=embedding_weights.shape[0], output_dim=embedding_weights.shape[1], weights=[embedding_weights], trainable=False, mask_zero=True)

def get_training_data():
    sentences_file = open("./processed_datasets/tri_train_x.txt", "r")
    categories_file = open("./processed_datasets/tri_train_y.txt", "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    categories = [int(line) for line in categories_file.readlines()]
    return tokenized_sentences, categories

def get_validation_data():
    sentences_file = open("./processed_datasets/tri_validation_x.txt", "r")
    categories_file = open("./processed_datasets/tri_validation_y.txt", "r")
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
    max_sentence_len = tf.ragged.constant(indexed_sentences).bounding_shape()[-1]
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return tf.constant(padded_sentences), max_sentence_len

# The index of the word 
pad_index = 0

print("Loading training and validation data...")

# Load the training data (tokenized sentences and labels)
x_train, y_train = get_training_data()
x_validation, y_validation = get_validation_data()

# Convert the data labels into fixed length tensors
y_train = tf.constant(y_train)
y_validation = tf.constant(y_validation)

print("Loaded dataset.")
print("Loading word embedding word to index mappings...")

# Word to index and index to word dictionary mappings 
word2index, index2word = load_word_mappings()

print("Loaded mappings.")
print("Converting tokenized words into word embedding indexes and padding sentence lengths.")

# Pad the data to a fixed length and convert words to indexes
x_train_padded, max_sentence_len = preprocess_sentences(word2index, x_train)
x_validation_padded, _ = preprocess_sentences(word2index, x_validation)

print("Data processed.")

# Set the log directory and create the tensorboard callback for viewing analytics
log_dir = "./logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
tb_callback = tf.keras.callbacks.TensorBoard(histogram_freq=1, log_dir=log_dir)

# Model 1 - LSTM with dropout and 2 hidden layers
# model = Sequential()
# model.add(Word2Vec(max_sentence_len))
# model.add(LSTM(512, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
# model.add(Dropout(0.2))
# model.add(Dense(512, activation='relu'))
# model.add(Dense(512, activation='relu'))
# model.add(Dense(5, activation='softmax'))
# optimizer = tf.keras.optimizers.Adam(lr=1e-3, decay=1e-5)
# model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
# model.fit(x=x_train_padded, y=y_train, batch_size=16, epochs=20, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback])

# Model 2 - LSTM with dropout and 10 hidden layers
# model = Sequential()
# model.add(Word2Vec(max_sentence_len))
# model.add(LSTM(256, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(64, activation='relu'))
# model.add(Dense(5, activation='softmax'))
# # optimizer = tf.keras.optimizers.Adam(lr=1e-4, decay=1e-5)
# optimizer = tf.keras.optimizers.Adam(lr=1e-4)
# model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
# model.fit(x=x_train_padded, y=y_train, batch_size=32, epochs=20, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback])

# # Model 3 - LSTM with dropout and 5 hidden layers -> 2nd best performing
# model = Sequential()
# model.add(Word2Vec(max_sentence_len))
# model.add(LSTM(256, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
# model.add(BatchNormalization())
# model.add(Dropout(0.25))
# model.add(Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(Dropout(0.25))
# model.add(Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(Dropout(0.25))
# model.add(Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(Dropout(0.25))
# model.add(Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(Dropout(0.25))
# model.add(Dense(32, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(Dropout(0.25))
# model.add(Dense(3, activation='softmax'))
# optimizer = tf.keras.optimizers.Adam(lr=1e-4)
# model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
# model.fit(x=x_train_padded, y=y_train, batch_size=128, epochs=20, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback])

print("Generating model...")

# Model 4 - Conv-LSTM -> Best performing
model = Sequential()
model.add(Word2Vec(max_sentence_len))
model.add(Conv1D(64, 3, kernel_regularizer=regularizers.l2(1e-4)))
model.add(AveragePooling1D(2))
model.add(BatchNormalization())
model.add(Conv1D(32, 3, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(AveragePooling1D(2))
model.add(LSTM(32, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
model.add(BatchNormalization())
model.add(Dropout(0.25))
model.add(Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(Dropout(0.25))
model.add(Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(Dropout(0.25))
model.add(Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(Dropout(0.25))
model.add(Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(Dropout(0.25))
model.add(Dense(32, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
model.add(Dropout(0.25))
model.add(Dense(3, activation='softmax'))
optimizer = tf.keras.optimizers.Adam(lr=1e-4)
model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])

print("Model built.")
print("Beginning training...")

model.fit(x=x_train_padded, y=y_train, batch_size=256, epochs=10, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback])

print("Training complete.")
