import tensorflow as tf
from keras.models import Model
from keras.layers import LSTM, BatchNormalization, Dropout, Dense, Bidirectional, concatenate, Input
from keras import regularizers

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

inputs = Input(shape=(None,300), name='embedding_input')
# embeddings = Word2Vec(max_sentence_len)(inputs)
x1 = get_big_parallel_model(inputs)
x2 = get_big_parallel_model(inputs)
x3 = get_big_parallel_model(inputs)
x4 = get_big_parallel_model(inputs)
shared = concatenate([x1, x2, x3, x4])
model = Model(inputs=inputs, outputs=shared)
model.load_weights("./models/20210224-102401/Uni+Bi-LSTM.hdf5", by_name=True)
model.summary()

file = open("./models/20210224-102401/no_embedding/Uni+Bi-LSTM.json", "w+")
model.save_weights("./models/20210224-102401/no_embedding/Uni+Bi-LSTM.hdf5")
file.write(model.to_json())
