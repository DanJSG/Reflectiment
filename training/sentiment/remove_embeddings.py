from keras.models import Sequential
from keras.layers import LSTM, Dropout, Dense, BatchNormalization, Conv1D, AveragePooling1D, Input
from keras import regularizers

model = Sequential()
model.add(Input(shape=(None, 300), name='embedding_input'))
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
model.add(Dense(1, activation='sigmoid'))

model.load_weights("./models/binary/20210219-014937/C-LSTM.hdf5", by_name=True)
model.summary()

file = open("./models/binary/20210219-014937/no_embedding/C-LSTM.json", "w+")
file.write(model.to_json())
model.save_weights("./models/binary/20210219-014937/no_embedding/C-LSTM.hdf5")
