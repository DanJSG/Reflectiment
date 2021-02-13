import nltk
from app.sentence import Sentence

class Document:
    def __init__(self, text: str):
        super().__init__()
        self.text: str = text
        self.sentences: list = []
        for sentence in nltk.sent_tokenize(text):
            self.sentences.append(Sentence(sentence))
