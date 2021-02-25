import tensorflow as tf

model: tf.keras.Sequential = tf.keras.models.model_from_json(open("./embedding.json", "r").read())
model.load_weights("./embedding.hdf5")
model.summary()
