import nltk
import string
from flask import current_app
from keras.preprocessing.sequence import pad_sequences
from app.word_mappings import get_word_index

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
        self.indexed: list = self._to_index()
        self.sentiment: str = self._get_sentiment()
    
    def _to_index(self) -> list:
        """ Convert the tokenized sentence into an indexed version.

        Returns:
            The indexed sentence as a list of integers
        """
        with current_app.app_context():
            indexed_sentence = []
            for word in self.tokens:
                indexed_sentence.append(get_word_index(current_app.word2index, word))
            padded_sentences = pad_sequences([indexed_sentence], maxlen=52, padding='post', value=0)
            return padded_sentences[0].tolist()
    
    def _get_sentiment(self):
        result = None
        with current_app.app_context():
            result = current_app.sentiment_analyzer.get_sentiment_classification(self.indexed)
        return result
