## File for exporting the neural network models to JSON configuration. For some reason doing the same thing 
## in the main training file throws an error.

from keras.models import Sequential
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Conv1D, AveragePooling1D
from keras import regularizers

# Binary classifier model
# model = Sequential()
# model.add(Embedding(input_length=52, input_dim=3000000, output_dim=300, trainable=False, mask_zero=True))
# model.add(Conv1D(64, 3, kernel_regularizer=regularizers.l2(1e-4)))
# model.add(AveragePooling1D(2))
# model.add(BatchNormalization())
# model.add(Conv1D(32, 3, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(LSTM(32, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
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
# model.add(Dense(1, activation='sigmoid'))

# Fine grained classifier model
# model = Sequential()
# model.add(Embedding(input_length=52, input_dim=3000000, output_dim=300, trainable=False, mask_zero=True))
# model.add(Conv1D(64, 3, kernel_regularizer=regularizers.l2(1e-4)))
# model.add(AveragePooling1D(2))
# model.add(BatchNormalization())
# model.add(Conv1D(32, 3, activation='relu', kernel_regularizer=regularizers.l2(1e-4)))
# model.add(LSTM(32, activation='tanh', recurrent_activation='sigmoid', recurrent_dropout=0, unroll=False, use_bias=True))
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
# model.add(Dense(5, activation='softmax'))

# Regression model
model = Sequential()
model.add(Embedding(input_length=52, input_dim=3000000, output_dim=300, trainable=False, mask_zero=True))
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
model.add(Dense(1, activation='relu'))

# file = open("./models/binary/20210219-014937/C-LSTM.json", "w+")
# file = open("./models/binary/20210220-000803/C-LSTM.json", "w+")
file = open("./models/binary/20210309-011610/C-LSTM.json", "w+")
file.write(model.to_json())


