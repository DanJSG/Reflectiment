from keras.layers import Input, GaussianNoise, LSTM, BatchNormalization, Dropout, Dense, Bidirectional, Embedding, concatenate
from keras.regularizers import l2
from keras import Model
import tensorflow as tf

max_sentence_len = 52
dropout_rate = 0.3

inputs = Input(shape=(None, 300), name='embedding_input')
embeddings = GaussianNoise(0.25)(inputs)

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

model = Model(inputs=inputs, outputs=x3)
model.summary()
tf.keras.utils.plot_model(model, show_shapes=True, show_dtype=True, show_layer_names=False, dpi=192)

model.load_weights("./models/20210304-161015/Uni+Bi-LSTM.hdf5", by_name=True)

model.save_weights("./models/20210304-161015/no_embeddings/Uni+Bi-LSTM.hdf5")

file = open("./models/20210304-161015/no_embeddings/Uni+Bi-LSTM.json", "w+")
file.write(model.to_json())


