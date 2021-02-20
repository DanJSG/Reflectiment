import nltk
from app.sentence import Sentence

class Document:
    """ A single document.

    Contains the original text and a list of sentences.

    Attributes:
        text: the original text
        sentences: the sentence objects that make up the doc
    """
    def __init__(self, text: str):
        super().__init__()
        self.text: str = text
        self.sentences = [Sentence(sentence) for sentence in nltk.sent_tokenize(text)]
