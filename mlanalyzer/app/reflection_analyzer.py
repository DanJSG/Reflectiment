import keras
import tensorflow as tf
from flask import current_app

class ReflectionAnalyzer():
    """ An analyzer for the reflection of a sentence.

    A class containing a loaded neural network model for reflection analysis and
    methods for analyzing a sentence.

    Attributes:
        labels: the potential classification labels
        model: the sentiment analysis neural network model
    """

    def __init__(self) -> None:
        self._json_path: str = "./models/reflection/Uni+Bi-LSTM.json"
        self._weights_path: str = "./models/reflection/Uni+Bi-LSTM.hdf5"
        self._configure_gpu()
        self.labels: list = ["descriptive", "quite reflective", "somewhat reflective", "reflective", "very reflective"]
        self.model: keras.Model = self._load_model()
        self._dummy_request()

    def get_reflection_score(self, embedded_sentence) -> str:
        """ Get the reflection score and label of a sentence.

        Takes a padded, indexed sentence and estimates its reflection score
        using the loaded neural network model.

        Args:
            embedded_sentence: the embedded word list

        Returns:
            The reflection score as a float and its associated reflection strength label

        """
        score: float = self.model.predict(embedded_sentence)[0][0]
        return float(score), self._get_reflection_label(score)

    def _get_reflection_label(self, score):
        """ Get the reflection label based on the reflection score.
        
        Args:
            score: the reflection score output of the neural network

        Returns:
            The reflection label string
        """
        if score >= 0 and score < 0.2:
            return self.labels[0]
        elif score >= 0.2 and score < 0.4:
            return self.labels[1]
        elif score >= 0.4 and score < 0.6:
            return self.labels[2]
        elif score >= 0.6 and score < 0.8:
            return self.labels[3]
        else:
            return self.labels[4]

    def _load_model(self) -> keras.Model:
        """ Load the machine learning model from the JSON and hdf5 files.
        Returns:
            A loaded and initialized Keras model
        """
        print("Loading model...")
        model: keras.Model = keras.models.model_from_json(open(self._json_path, "r").read())
        model.load_weights(self._weights_path)
        print("Model loaded.")
        model.summary()
        return model

    def _dummy_request(self) -> None:
        """ Send a dummy classification request to initialize the neural network."""
        with current_app.app_context():
            embedded = current_app.word_embedder.get_embeddings([2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999])
            self.get_reflection_score(embedded)
    
    @staticmethod
    def _configure_gpu() -> None:
        """ Initialize GPU memory growth to optimize performance."""
        physical_devices = tf.config.experimental.list_physical_devices('GPU')
        tf.config.experimental.set_memory_growth(physical_devices[0], True)
