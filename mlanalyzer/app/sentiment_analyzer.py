import keras
from flask import current_app
from app.analyzer import Analyzer

class SentimentAnalyzer(Analyzer):
    """ An analyzer for the sentiment of a sentence.

    A class containing a loaded neural network model for sentiment analysis and
    methods for analyzing a sentence.

    Attributes:
        labels: the potential classification labels
        model: the sentiment analysis neural network model
    """

    def __init__(self) -> None:
        self._json_path: str = "./models/sentiment/C-LSTM.json"
        self._weights_path: str = "./models/sentiment/C-LSTM.hdf5"
        self._configure_gpu()
        self.labels: list = ["negative", "positive"]
        self.model: keras.Sequential = self._load_model()
        self._dummy_request()

    def get_sentiment_classification(self, embedded_sentence) -> str:
        """ Get the sentiment classification of a sentence.

        Takes a padded, indexed sentence and estimates its sentiment classification
        using the loaded neural network model.

        Args:
            embedded_sentence: the embedded word list

        Returns:
            A string label representing the estimated classification

        """
        score: float = self.model.predict(embedded_sentence)[0][0]
        return self._get_class_name(self._classify_score(score))

    def _dummy_request(self) -> None:
        """ Send a dummy classification request to initialize the neural network."""
        with current_app.app_context():
            embedded = current_app.word_embedder.get_embeddings([2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999, 2999999])
            self.get_sentiment_classification(embedded)
    
    def _get_class_name(self, class_index) -> str:
        """ Get the class name associated with a class index.

        Returns:
            A string label of the estimated class
        """
        return self.labels[class_index]

    def _classify_score(self, score) -> int:
        """ Classify the output score into an integer index. 
        
        Args:
            score: the output score of the classifier

        Return:
            An integer index representing the estimated class
        """
        return 1 if score > 0.5 else 0
