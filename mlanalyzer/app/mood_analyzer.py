import keras
from flask import current_app
from app.analyzer import Analyzer

class MoodAnalyzer(Analyzer):
    """ An analyzer for the mood of a sentence.

    A class containing a loaded neural network model for mood analysis and
    methods for analyzing a sentence.

    Attributes:
        labels: the potential classification labels
        model: the sentiment analysis neural network model
    """

    def __init__(self) -> None:
        self._json_path: str = "./models/mood/Uni+Bi-LSTM.json"
        self._weights_path: str = "./models/mood/Uni+Bi-LSTM.hdf5"
        self._configure_gpu()
        self.labels: list = ["joy", "anger", "fear", "sadness"]
        self.model: keras.Model = self._load_model()
        self._dummy_request()

    def get_mood_classification(self, embedded_sentence) -> str:
        """ Get the mood classification of a sentence.

        Takes a padded, indexed sentence and estimates its mood classification
        using the loaded neural network model.

        Args:
            embedded_sentence: the embedded word list

        Returns:
            A dictionary of the emotion labels with corresponding scores

        """
        scores: float = self.model.predict(embedded_sentence)[0].tolist()
        labelled_scores = {}
        for i in range(len(self.labels)):
            labelled_scores[self.labels[i]] = scores[i]
        return labelled_scores

    def _dummy_request(self) -> None:
        """ Send a dummy classification request to initialize the neural network."""
        with current_app.app_context():
            embedded = current_app.word_embedder.get_embeddings([2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999])
            self.get_mood_classification(embedded)
