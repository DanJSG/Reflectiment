from keras.layers import Input, Embedding, Bidirectional, LSTM, Dropout, Dense, BatchNormalization, GaussianNoise, concatenate
from keras.regularizers import l2
from keras import Model
import tensorflow as tf


def get_parallel_branch(embeddings):

    dropout_rate = 0.35

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

    x4 = Dense(48, activation='relu')(combined)
    x4 = Dropout(dropout_rate)(x4)
    x4 = Dense(32, activation='relu')(x4)
    x4 = Dropout(dropout_rate)(x4)
    x4 = Dense(1, activation='relu')(x4)

    return x4

inputs = Input(shape=(None, 300), name='embedding_input')
embeddings = GaussianNoise(0.25)(inputs)

x1 = get_parallel_branch(embeddings)
x2 = get_parallel_branch(embeddings)
x3 = get_parallel_branch(embeddings)
x4 = get_parallel_branch(embeddings)
x5 = get_parallel_branch(embeddings)
x6 = get_parallel_branch(embeddings)

output = concatenate([x1, x2, x3, x4, x5, x6])
model = Model(inputs=inputs, outputs=output)
model.summary()
tf.keras.utils.plot_model(model, show_shapes=True, show_dtype=True, show_layer_names=False, dpi=192)

model.summary()

model.load_weights("./models/20210310-163809/C-LSTM.hdf5", by_name=True)

file = open("./models/20210310-163809/no_embeddings/C-LSTM.json", "w+")
file.write(model.to_json())

model.save_weights("./models/20210310-163809/no_embeddings/C-LSTM.hdf5")
