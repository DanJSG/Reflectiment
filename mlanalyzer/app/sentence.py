import nltk
import string
from numpy.random import rand
from flask import current_app

class Sentence:
    def __init__(self, text: str):
        super().__init__()
        self.text: str = text
        self.tokens: list = nltk.word_tokenize(text.translate(str.maketrans('','', string.punctuation)).lower())
        self.vectors = self.vectorize()
        
    def vectorize(self):
        vectors = []
        with current_app.app_context():
            model = current_app.model
            for token in self.tokens:
                try:
                    vectors.append(model[token].array)
                except:
                    vectors.append(rand(300))
        return vectors
