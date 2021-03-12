from keras.layers import Input, Embedding, Bidirectional, LSTM, Dropout, Dense, BatchNormalization, GaussianNoise, concatenate
from keras.preprocessing.sequence import pad_sequences
from keras import Model, backend as K
from keras.regularizers import l2
from nltk import word_tokenize
import tensorflow as tf
import numpy as np
import datetime
import json
import os

def load_word_mappings():
    word2index = json.loads(open("word2index.json", "r").read())
    return word2index

def initialize_gpu():
    physical_devices = tf.config.experimental.list_physical_devices('GPU')
    print("GPUs available: ", len(physical_devices))
    tf.config.experimental.set_memory_growth(physical_devices[0], True)

def Word2Vec(input_length):
    embedding_weights = np.load(open("weights_file.bin", "rb"))
    print("Weight shape: " + str(embedding_weights.shape))
    return Embedding(input_length=input_length, input_dim=embedding_weights.shape[0], output_dim=embedding_weights.shape[1], weights=[embedding_weights], trainable=False, mask_zero=True)

def get_data(sentences_path, scores_path):
    print("Loading training data...")
    sentences_file = open(sentences_path, "r", encoding='utf8')
    scores_file = open(scores_path, "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    scores = [float(line.strip("\n")) for line in scores_file.readlines()]
    return tokenized_sentences, scores

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

def preprocess_sentences(word2index, sentences, max_sentence_len):
    indexed_sentences = index_sentence_words(word2index, sentences)
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return padded_sentences.tolist()

def r(y_true, y_pred):
    true_sub = y_true - K.mean(y_true)
    pred_sub = y_pred - K.mean(y_pred)
    return K.mean(K.sum(true_sub * pred_sub) / K.sqrt(K.sum(K.pow(true_sub, 2)) * K.sum(K.pow(pred_sub, 2))))

max_sentence_len = 52

print("Starting to load all data...")

train_sentences, y_train = get_data("./datasets/extended/sentences.train.txt", "./datasets/extended/scores.train.txt")
validation_sentences, y_validation = get_data("./datasets/extended/sentences.test.txt", "./datasets/extended/scores.test.txt")
word2index = load_word_mappings()

print("Processing data...")

x_train_padded = preprocess_sentences(word2index, train_sentences, max_sentence_len)
x_validation_padded = preprocess_sentences(word2index, validation_sentences, max_sentence_len)

print("Initializing training log...")

# Set the log directory and create the tensorboard callback for viewing analytics
log_dir = "./logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
tb_callback = tf.keras.callbacks.TensorBoard(histogram_freq=1, log_dir=log_dir)

print("Generating model...")

dropout_rate = 0.3

inputs = Input(shape=(52, ))

embeddings = Word2Vec(max_sentence_len)(inputs)
embeddings = GaussianNoise(0.25)(embeddings)

x1 = LSTM(64, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True)(embeddings)
x1 = BatchNormalization()(x1)
x1 = Dropout(dropout_rate)(x1)
x1 = Dense(128, activation='relu', kernel_regularizer=l2(1e-4))(x1)
x1 = Dropout(dropout_rate)(x1)
x1 = Dense(128, activation='relu', kernel_regularizer=l2(1e-4))(x1)
x1 = Dropout(dropout_rate)(x1)
x1 = Dense(64, activation='relu', kernel_regularizer=l2(1e-4))(x1)
x1 = Dropout(dropout_rate)(x1)
x1 = Dense(32, activation='relu', kernel_regularizer=l2(1e-4))(x1)
x1 = Dropout(dropout_rate)(x1)
x1 = Dense(16, activation='relu', kernel_regularizer=l2(1e-4))(x1)
x1 = Dropout(dropout_rate)(x1)

x2 = Bidirectional(LSTM(32, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))(embeddings)
x2 = BatchNormalization()(x2)
x2 = Dropout(dropout_rate)(x2)
x2 = Dense(128, activation='relu', kernel_regularizer=l2(1e-4))(x2)
x2 = Dropout(dropout_rate)(x2)
x2 = Dense(128, activation='relu', kernel_regularizer=l2(1e-4))(x2)
x2 = Dropout(dropout_rate)(x2)
x2 = Dense(64, activation='relu', kernel_regularizer=l2(1e-4))(x2)
x2 = Dropout(dropout_rate)(x2)
x2 = Dense(64, activation='relu', kernel_regularizer=l2(1e-4))(x2)
x2 = Dropout(dropout_rate)(x2)
x2 = Dense(32, activation='relu', kernel_regularizer=l2(1e-4))(x2)
x2 = Dropout(dropout_rate)(x2)

combined = concatenate([x1, x2])

x3 = Dense(48, activation='relu')(combined)
x3 = Dense(1, activation='relu')(x3)
# x3 = Dense(5, activation='softmax')(x3)
# x3 = Dense(1, activation='sigmoid')(x3)

optimizer = tf.keras.optimizers.Adam(lr=1e-4)

model = Model(inputs=inputs, outputs=x3)
# model.compile(loss=r_loss, optimizer=optimizer, metrics=[r, 'mean_squared_error'])
model.compile(loss='mean_squared_error', optimizer=optimizer, metrics=[r, 'mean_squared_error'])
# model.compile(loss='sparse_categorical_crossentropy', optimizer=optimizer, metrics=['accuracy', tfa.metrics.CohenKappa(5, sparse_labels=True)])
# model.compile(loss='binary_crossentropy', optimizer=optimizer, metrics=['accuracy', tfa.metrics.CohenKappa(2)])
model.summary()

tb_callback = tf.keras.callbacks.TensorBoard(histogram_freq=1, log_dir=log_dir)

timestamp_str = str(datetime.datetime.now().strftime('%Y%m%d-%H%M%S'))
os.mkdir(f"./models/{timestamp_str}")
weights_filepath = f"./models/{timestamp_str}/C-LSTM.hdf5"

# Callback used to store the model as training goes along. Stores only the best performing model.
model_checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
    filepath=weights_filepath,
    save_weights_only=True,
    monitor='val_r',
    mode='max',
    save_best_only=True
)

print("Starting training...")
model.fit(x=x_train_padded, y=y_train, batch_size=16, epochs=60, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback, model_checkpoint_callback])
# model.fit(x=x_train_padded, y=y_train, batch_size=32, epochs=25, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[tb_callback])

