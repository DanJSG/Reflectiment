import json

def load_word_mappings() -> tuple:
    """ Load word mappings into memory.
    
    Load the word to index and index to word mappings from the JSON files
    containing the vocabulary.
    
    Returns:
        The word to index mapping and the index to word mapping
    """
    word2index = json.loads(open("./resources/word2index.json", "r").read())
    index2word = json.loads(open("./resources/index2word.json", "r").read())
    return word2index, index2word

def get_word_index(word2index: dict, word: str) -> int:
    """ Get the word index from the vocabulary.

    Get the index of a word from the vocabulary, or an index representing 
    an unknown word if it is not in the vocabulary.

    Args:
        word2index: word2index map containing the vocabulary
        word: the word to find the index of

    Returns:
        An integer index representing the word, or 2999999 if the word is
        out of the vocab.
    """
    try:
        return word2index[word]
    except:
        return 2999999


