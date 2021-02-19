import gensim.downloader
from gensim.models.keyedvectors import KeyedVectors

def load_embedding_model():
    print("Loading Google News word embedding model...")
    # model = gensim.downloader.load("word2vec-google-news-300")
    model = KeyedVectors.load_word2vec_format('./models/GoogleNews-vectors-negative300.bin', binary=True)
    print("Model loaded successfully!")
    print("Model has " + str(len(model.vocab)) + " words in its vocabulary.")
    return model
