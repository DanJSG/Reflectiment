import json
import os
import datetime
import tensorflow as tf
import numpy as np
from keras.models import Sequential, Model, Input
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Conv1D, AveragePooling1D, concatenate, Bidirectional
from nltk.tokenize import word_tokenize
from keras.preprocessing.sequence import pad_sequences
from keras import regularizers
from keras import backend as K

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
    print("Loading training data...")
    sentences_file = open("./processed_datasets/sem_eval/correlation/x.combined.train.txt", "r", encoding='utf8')
    scores_file = open("./processed_datasets/sem_eval/correlation/y.combined.train.txt")
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
    print("Loading validation data...")
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

def preprocess_sentences(word2index, sentences, max_sentence_len):
    indexed_sentences = index_sentence_words(word2index, sentences)
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return padded_sentences.tolist(), max_sentence_len

def get_big_parallel_model(embeddings):
    
    dropout_rate = 0.25

    x1 = LSTM(64, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True)(embeddings)
    x1 = BatchNormalization()(x1)
    x1 = Dropout(dropout_rate)(x1)
    x1 = Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x1)
    x1 = Dropout(dropout_rate)(x1)
    x1 = Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x1)
    x1 = Dropout(dropout_rate)(x1)
    x1 = Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x1)
    x1 = Dropout(dropout_rate)(x1)
    x1 = Dense(32, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x1)
    x1 = Dropout(dropout_rate)(x1)
    x1 = Dense(16, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x1)
    x1 = Dropout(dropout_rate)(x1)

    x2 = Bidirectional(LSTM(32, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))(embeddings)
    x2 = BatchNormalization()(x2)
    x2 = Dropout(dropout_rate)(x2)
    x2 = Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x2)
    x2 = Dropout(dropout_rate)(x2)
    x2 = Dense(128, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x2)
    x2 = Dropout(dropout_rate)(x2)
    x2 = Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x2)
    x2 = Dropout(dropout_rate)(x2)
    x2 = Dense(64, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x2)
    x2 = Dropout(dropout_rate)(x2)
    x2 = Dense(32, activation='relu', kernel_regularizer=regularizers.l2(1e-4))(x2)
    x2 = Dropout(dropout_rate)(x2)
    combined = concatenate([x1, x2])

    x3 = Dense(48, activation='relu')(combined)
    x3 = Dense(32, activation='relu')(x3)
    x3 = Dense(1, activation='relu')(x3)
    return x3

def r(y_true, y_pred):
    true_mean = K.mean(y_true)
    pred_mean = K.mean(y_pred)
    top = K.sum((y_true - true_mean) * (y_pred - pred_mean))
    bottom = K.sqrt(K.sum(K.pow(y_true - true_mean, 2) * K.sum(K.pow(y_pred - pred_mean, 2))))
    return K.mean(top / bottom)
    
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

max_sentence_len = 52

# Pad the data to a fixed length and convert words to indexes
x_train_padded, _ = preprocess_sentences(word2index, x_train, max_sentence_len)
x_validation_padded, _ = preprocess_sentences(word2index, x_validation, max_sentence_len)

print("Data processed.")

# Set the log directory and create the tensorboard callback for viewing analytics
log_dir = "./logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
tb_callback = tf.keras.callbacks.TensorBoard(histogram_freq=1, log_dir=log_dir)

print("Generating model...")

inputs = Input(shape=(52,))
embeddings = Word2Vec(max_sentence_len)(inputs)
x1 = get_big_parallel_model(embeddings)
x2 = get_big_parallel_model(embeddings)
x3 = get_big_parallel_model(embeddings)
x4 = get_big_parallel_model(embeddings)
shared = concatenate([x1, x2, x3, x4])
model = Model(inputs=inputs, outputs=shared)

model.summary()
tf.keras.utils.plot_model(model, show_shapes=True, show_dtype=True, show_layer_names=False, dpi=192)

optimizer = tf.keras.optimizers.Adam(lr=1e-4)
model.compile(loss='mean_squared_error', optimizer=optimizer, metrics=[r, 'mean_squared_error'])

print("Model built.")
print("Beginning training...")

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

# Train the model to the training set and validate against the validation set.
# model.fit(x=x_train_padded, y=y_train, batch_size=8, epochs=50, verbose=1, shuffle=True)
# model.fit(x=x_train_padded, y=y_train, batch_size=16, epochs=50, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation))
model.fit(x=x_train_padded, y=y_train, batch_size=4, epochs=50, verbose=1, shuffle=True, validation_data=(x_validation_padded, y_validation), callbacks=[model_checkpoint_callback, tb_callback])

print("Training complete.")
