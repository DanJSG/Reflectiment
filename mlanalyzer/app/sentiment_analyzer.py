import keras
import tensorflow as tf

class SentimentAnalyzer():
    
    def __init__(self) -> None:
        self._json_path = "./models/sentiment/C-LSTM.json"
        self._weights_path = "./models/sentiment/C-LSTM.hdf5"
        self._configure_gpu()
        self.model = self.load_model()

    def load_model(self) -> keras.Model:
        print("Loading model...")
        model: keras.Model = keras.models.model_from_json(open(self._json_path, "r").read())
        model.load_weights(self._weights_path)
        print("Model loaded. Architecture: ")
        model.summary()
        return model
    
    @staticmethod
    def _configure_gpu() -> None:
        physical_devices = tf.config.experimental.list_physical_devices('GPU')
        tf.config.experimental.set_memory_growth(physical_devices[0], True)
