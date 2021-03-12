import tensorflow as tf
from keras.preprocessing.sequence import pad_sequences
from pprint import pprint

model: tf.keras.Sequential = tf.keras.models.model_from_json(open("./embedding.json", "r").read())
model.load_weights("./embedding.hdf5")
model.summary()

input = [[152, 412, 542, 111, 53, 5, 353, 8751]]
padded_input = pad_sequences(input, maxlen=52, padding='post').tolist()

result = model.predict(input)
padded_result = model.predict(padded_input)

pprint(result.tolist())
print(len(result.tolist()[0]))
print(len(padded_result.tolist()[0]))
