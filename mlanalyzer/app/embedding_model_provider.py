from gensim.models.keyedvectors import KeyedVectors

def load_embedding_model():
    print("Loading GoogleNews word embedding model...")
    model = KeyedVectors(300).load_word2vec_format('./models/GoogleNews-vectors-negative300.bin', binary=True)
    print("Model contains " + str(len(model.vocab)) + " words.")
    return model
