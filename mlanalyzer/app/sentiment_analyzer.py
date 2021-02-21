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
        self._json_path: str = "./models/sentiment/C-LSTM.json"
        self._weights_path: str = "./models/sentiment/C-LSTM.hdf5"
        self._configure_gpu()
        self.labels: list = ["negative", "positive"]
        self.model: keras.Sequential = self._load_model()
        self._dummy_request()

    def get_sentiment_classification(self, indexed_sentence) -> str:
        score: float = self.model.predict([indexed_sentence])[0][0]
        return self._get_class_name(self._classify_score(score))

    def _load_model(self) -> keras.Model:
        """ Load the machine learning model from the JSON and hdf5 files.
        Returns:
            A loaded and initialized Keras model
        """
        print("Loading model...")
        model: keras.Sequential = keras.models.model_from_json(open(self._json_path, "r").read())
        model.load_weights(self._weights_path)
        print("Model loaded.")
        model.summary()
        return model

    def _dummy_request(self) -> None:
        self.get_sentiment_classification([2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999])
    
    @staticmethod
    def _configure_gpu() -> None:
        """ Initialize GPU memory growth to optimize performance."""
        physical_devices = tf.config.experimental.list_physical_devices('GPU')
        tf.config.experimental.set_memory_growth(physical_devices[0], True)

    @staticmethod
    def _classify_score(score) -> int:
        return 1 if score > 0.5 else 0

    @staticmethod
    def _get_class_name(class_index) -> str:
        return 'positive' if class_index == 1 else 'negative'
