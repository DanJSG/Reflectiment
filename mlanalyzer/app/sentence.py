import nltk
import string
from flask import current_app
from keras.preprocessing.sequence import pad_sequences
from app.word_mappings import get_word_index
import tensorflow as tf

class Sentence:
    """ A single sentence.

    Contains the original text, the tokenized version in lower case with punctuation removed and
    an indexed version of the sentence where the words are replaced with integers.

    Attributes:
        text: the original text
        tokens: tokenized word list of the sentence
        indexed: indexed version of the tokenized word list
    """
    def __init__(self, text: str):
        super().__init__()
        self.text: str = text
        self.tokens: list = nltk.word_tokenize(text.translate(str.maketrans('','', string.punctuation)).lower())
        indexed: list = self._to_index()
        indexed_padded = pad_sequences([indexed], maxlen=52, padding='post', value=0)[0].tolist()
        embedded: list = self._to_embedding(indexed)
        embedded_padded: list = self._to_embedding(indexed_padded)
        self.sentiment: str = self._get_sentiment(embedded_padded)
        self.mood: dict = self._get_mood(embedded)
        self.mood_label, self.mood_score = self._get_strongest_mood()
    
    def _to_index(self) -> list:
        """ Convert the tokenized sentence into an indexed version.

        Returns:
            The indexed sentence as a list of integers
        """
        with current_app.app_context():
            indexed_sentence = []
            for word in self.tokens:
                indexed_sentence.append(get_word_index(current_app.word2index, word))
            return indexed_sentence
    
    def _to_embedding(self, indexed) -> list:
        """ Convert the indexed sentence into an array of 300-dimension word embedding vectors.

        Returns:
            The list of word embeddings
        """
        with current_app.app_context():
            return current_app.word_embedder.get_embeddings(indexed)

    def _get_sentiment(self, embedded):
        """ Get the sentiment classification label of the sentence.

        Fetches the sentiment analysis model from the current application context and guesses the
        sentiment of the sentence, returning a classification label.

        Returns:
            The string classification label
        """
        with current_app.app_context():
            result = current_app.sentiment_analyzer.get_sentiment_classification(embedded)
            return result

    def _get_mood(self, embedded):
        with current_app.app_context():
            return current_app.mood_analyzer.get_mood_classification(embedded)

    def _get_strongest_mood(self):
        label = max(self.mood, key=lambda key: self.mood[key])
        score = self.mood[label]
        return label, score
