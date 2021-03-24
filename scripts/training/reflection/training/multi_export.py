from keras.layers import Input, Embedding, Bidirectional, LSTM, Dropout, Dense, BatchNormalization, GaussianNoise, concatenate
from keras.regularizers import l2
from keras import Model


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

inputs = Input(shape=(52, ))

embeddings = Embedding(input_length=52, input_dim=3000000, output_dim=300, trainable=False, mask_zero=True)(inputs)
embeddings = GaussianNoise(0.25)(embeddings)

x0 = get_parallel_branch(embeddings)
x1 = get_parallel_branch(embeddings)
x2 = get_parallel_branch(embeddings)
x3 = get_parallel_branch(embeddings)
x4 = get_parallel_branch(embeddings)
x5 = get_parallel_branch(embeddings)
x6 = get_parallel_branch(embeddings)

output = concatenate([x0, x1, x2, x3, x4, x5, x6])

model = Model(inputs=inputs, outputs=output)

model.summary()

file = open("./models/20210323-131429/C-LSTM.json", "w+")
file.write(model.to_json())
