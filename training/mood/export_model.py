## File for exporting the neural network models to JSON configuration. For some reason doing the same thing 
## in the main training file throws an error.

from keras.models import Model
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Bidirectional, concatenate, Input
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

max_sentence_len = 52

inputs = Input(shape=(52,))
embeddings = Embedding(input_length=52, input_dim=3000000, output_dim=300, trainable=False, mask_zero=True)(inputs)
x1 = get_big_parallel_model(embeddings)
x2 = get_big_parallel_model(embeddings)
x3 = get_big_parallel_model(embeddings)
x4 = get_big_parallel_model(embeddings)
shared = concatenate([x1, x2, x3, x4])
model = Model(inputs=inputs, outputs=shared)

file = open("./models/20210224-102401/Uni+Bi-LSTM.json", "w+")
file.write(model.to_json())
