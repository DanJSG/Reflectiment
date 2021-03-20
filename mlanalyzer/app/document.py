import nltk
from app.sentence import Sentence
import re

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
        pattern = re.compile(r"[a-zA-Z0-9].*")
        self.sentences = [Sentence(sentence) for sentence in nltk.sent_tokenize(text) if pattern.match(sentence)]
