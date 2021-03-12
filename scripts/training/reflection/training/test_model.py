from keras.preprocessing.sequence import pad_sequences
from keras import backend as K
from keras import models
from keras import Model
from nltk import word_tokenize
import tensorflow as tf
import json

def load_word_mappings():
    word2index = json.loads(open("word2index.json", "r").read())
    return word2index

def initialize_gpu():
    physical_devices = tf.config.experimental.list_physical_devices('GPU')
    print("GPUs available: ", len(physical_devices))
    tf.config.experimental.set_memory_growth(physical_devices[0], True)

def get_data(sentences_path, scores_path):
    print("Loading training data...")
    sentences_file = open(sentences_path, "r", encoding='utf8')
    scores_file = open(scores_path, "r")
    tokenized_sentences = [word_tokenize(line) for line in sentences_file.readlines()]
    scores = [float(line.strip("\n")) for line in scores_file.readlines()]
    return tokenized_sentences, scores

def get_word_index(word2index, word):
    try:
        return word2index[word]
    except:
        return 2999999

def index_sentence_words(word2index, sentences):
    indexed_sentences = []
    for sentence in sentences:
        indexed_sentence = []
        for word in sentence:
            indexed_sentence.append(get_word_index(word2index, word))
        indexed_sentences.append(indexed_sentence)
    return indexed_sentences

def preprocess_sentences(word2index, sentences, max_sentence_len):
    indexed_sentences = index_sentence_words(word2index, sentences)
    padded_sentences = pad_sequences(indexed_sentences, maxlen=max_sentence_len, padding='post', value=0)
    return padded_sentences.tolist()

def r(y_true, y_pred):
    true_sub = y_true - K.mean(y_true)
    pred_sub = y_pred - K.mean(y_pred)
    return K.mean(K.sum(true_sub * pred_sub) / K.sqrt(K.sum(K.pow(true_sub, 2)) * K.sum(K.pow(pred_sub, 2))))

word2index = load_word_mappings()
test_x, test_y = get_data("./datasets/extended/sentences.test.txt", "./datasets/extended/scores.test.txt")
test_x = preprocess_sentences(word2index, test_x, 52)

model: Model = models.model_from_json(open("./models/20210304-161015/Uni+Bi-LSTM.json", "r").read())
model.load_weights("./models/20210304-161015/Uni+Bi-LSTM.hdf5")
model.compile(loss='mean_squared_error', metrics=[r, 'mean_squared_error'])

model.evaluate(test_x, test_y)


