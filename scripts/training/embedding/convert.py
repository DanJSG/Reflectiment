import tensorflow as tf
import numpy as np
from keras.models import Sequential, Model, Input
from keras.layers import LSTM, Dropout, Dense, Embedding, BatchNormalization, Conv1D, AveragePooling1D, concatenate, Bidirectional

print("Loading weights...")

embedding_weights = np.load(open("weights_file.bin", "rb"))

print("Generating model...")

model: Sequential = Sequential()
model.add(Embedding(input_length=52, input_dim=embedding_weights.shape[0], output_dim=embedding_weights.shape[1], weights=[embedding_weights], trainable=False, mask_zero=True))

model.summary()
tf.keras.utils.plot_model(model, show_shapes=True, show_dtype=True, show_layer_names=False, dpi=192)

file = open("./embedding.json", "w+")
model.save_weights("./embedding.hdf5", save_format='h5')
file.write(model.to_json())
