import json
import os
import datetime
import tensorflow as tf
import numpy as np
from keras.models import Sequential
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Conv1D, AveragePooling1D
from nltk.tokenize import word_tokenize
from keras.preprocessing.sequence import pad_sequences
from keras import regularizers
import keras.backend as K

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
    print("Weight shape: " + str(embedding_weights.shape))
    return Embedding(input_length=input_length, input_dim=embedding_weights.shape[0], output_dim=embedding_weights.shape[1], weights=[embedding_weights], trainable=False, mask_zero=True)

def get_training_data():
    # Comment/uncomment duplicate vars depending on the set of training data to use
    # sentences_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_train_x.txt", "r")
    # categories_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_train_y.txt", "r")
    # sentences_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_train_x.txt", "r")
    # categories_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_train_y.txt", "r")
    sentences_file = open("./processed_datasets/sentiment_treebank_ext/regression/train_x.txt", "r")
    categories_file = open("./processed_datasets/sentiment_treebank_ext/regression/train_y.txt", "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    # categories = [int(line) for line in categories_file.readlines()]
    categories = [float(line) for line in categories_file.readlines()]
    return tokenized_sentences, categories

def get_validation_data():
    # Comment/uncomment duplicate vars depending on the set of validation data to use
    # sentences_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_validation_x.txt", "r")
    # categories_file = open("./processed_datasets/sentiment_treebank_ext/binary/tri_validation_y.txt", "r")
    # sentences_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_validation_x.txt", "r")
    # categories_file = open("./processed_datasets/sentiment_treebank_ext/fine_grained/five_validation_y.txt", "r")
    sentences_file = open("./processed_datasets/sentiment_treebank_ext/regression/validation_x.txt", "r")
    categories_file = open("./processed_datasets/sentiment_treebank_ext/regression/validation_y.txt", "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    # categories = [[int(line)] for line in categories_file.readlines()]
    categories = [[float(line)] for line in categories_file.readlines()]
    return tokenized_sentences, categories

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
    max_sentence_len = tf.ragged.constant(indexed_sentences).bounding_shape()[-1]
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return padded_sentences.tolist(), max_sentence_len

def r(y_true, y_pred):
    true_sub = y_true - K.mean(y_true)
    pred_sub = y_pred - K.mean(y_pred)
    return K.mean(K.sum(true_sub * pred_sub) / K.sqrt(K.sum(K.pow(true_sub, 2)) * K.sum(K.pow(pred_sub, 2))))

# The index of the word 
pad_index = 0

print("Loading training and validation data...")

# Load the training data (tokenized sentences and labels)
x_train, y_train = get_training_data()
x_validation, y_validation = get_validation_data()

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

print("Generating model...")

# Model 4 - Conv-LSTM -> Best performing
# This is a Deep Convolutional Long Short Term Memory model. It uses two 1D convolution layers to
# extract grouped (but ordered) sentence features, then uses this as the input to an LSTM layer which
# helps to contextualize each of these features. The output of this then proceeds through 5 fully
# connected layers before arriving at an output layer. Dropout and an L2 regularizer are used to 
# help avoid the model from overfitting.
model = Sequential()
model.add(Word2Vec(max_sentence_len))
model.add(Conv1D(64, 3, kernel_regularizer=regularizers.l2(1e-4)))
model.add(AveragePooling1D(2))
model.add(BatchNormalization())
model.add(Conv1D(32, 3, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
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
# Params for different classification types: 
#   - Binary classification: (1, activation='sigmoid') 
#   - Five category fine-grained classification: (5, activation='softmax')
#   - Three category fine-grained classification: (3, activation='softmax')
#   - Regression: (1, activation='relu')
# model.add(Dense(1, activation='sigmoid'))
# model.add(Dense(5, activation='softmax'))
model.add(Dense(1, activation='relu'))
optimizer = tf.keras.optimizers.Adam(lr=1e-4)
# Loss for different classification types:
#   - Binary classification: binary_crossentropy
#   - Fine grained classification: sparse_categorical_crossentropy
#   - Regression: mean_squared_error
model.compile(loss='mean_squared_error', optimizer=optimizer, metrics=[r, 'mean_squared_error'])
# model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
# model.compile(loss='binary_crossentropy', optimizer=optimizer, metrics=['accuracy'])

print("Model built.")
print("Beginning training...")

timestamp_str = str(datetime.datetime.now().strftime('%Y%m%d-%H%M%S'))
os.mkdir(f"./models/binary/{timestamp_str}")

weights_filepath = f"./models/binary/{timestamp_str}/C-LSTM.hdf5"

# Callback used to store the model as training goes along. Stores only the best performing model.
model_checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
    filepath=weights_filepath,
    save_weights_only=True,
    monitor='val_accuracy',
    mode='max',
    save_best_only=True
)

# Train the model to the training set and validate against the validation set.
model.fit(x=x_train_padded, y=y_train, batch_size=32, epochs=50, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[model_checkpoint_callback, tb_callback])

print("Training complete.")
