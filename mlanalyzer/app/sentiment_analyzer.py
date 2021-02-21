import keras
import tensorflow as tf

class SentimentAnalyzer():
    """ An analyzer for the sentiment of a sentence.

    A class containing a loaded neural network model for sentiment analysis and
    methods for analyzing a sentence.

    Attributes:
        _json_path: the path to the JSON format neural network model
        _weights_path: the path to the .hdf5 format neural network model weights
    """

    def __init__(self) -> None:
        self._json_path = "./models/sentiment/C-LSTM.json"
        self._weights_path = "./models/sentiment/C-LSTM.hdf5"
        self._configure_gpu()
        self.model = self._load_model()

    def _load_model(self) -> keras.Model:
        """ Load the machine learning model from the JSON and hdf5 files.
        Returns:
            A loaded and initialized Keras model
        """
        print("Loading model...")
        model: keras.Model = keras.models.model_from_json(open(self._json_path, "r").read())
        model.load_weights(self._weights_path)
        print("Model loaded. Architecture: ")
        model.summary()
        return model
    
    @staticmethod
    def _configure_gpu() -> None:
        """ Initialize GPU memory growth to optimize performance."""
        physical_devices = tf.config.experimental.list_physical_devices('GPU')
        tf.config.experimental.set_memory_growth(physical_devices[0], True)
