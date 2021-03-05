import keras
import tensorflow as tf
from .analyzer import Analyzer

class WordEmbedder(Analyzer):
    """ A converter which converts indexed word lists into 300-dimensional vector lists.

    Converts words using a Keras model based on the Google News trained Word2Vec embeddings.

    Attributes:
        model: the Keras embedding model
    """
    def __init__(self) -> None:
        self._json_path = "./models/embedding/embedding.json"
        self._weights_path = "./models/embedding/embedding.hdf5"
        self._configure_gpu()
        self.model = self._load_model()
        self._dummy_request()

    def get_embeddings(self, tokenized_sentence) -> list:
        """ Get the word embeddings for each word in an indexed sentence.

        Returns:
            A list of 300-dimensional word embedding vectors

        """
        wrapped_embeddings = self.model.predict(tokenized_sentence).tolist()
        embeddings = [[word[0] for word in wrapped_embeddings]]
        return tf.convert_to_tensor(embeddings)

    def _dummy_request(self) -> None:
        """ Send a dummy prediction request to the Keras model to initialize it."""
        self.model.predict([[150]])
