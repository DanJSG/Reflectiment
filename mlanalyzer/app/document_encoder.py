from app.document import Document

def encode_document(doc: Document) -> dict:
    """ Encode a document as a JSON serializable dictionary. 
    
    Encodes a document to a JSON serializable dict containing the document
    text, sentences and each sentences sentiment labels.
    
    Args:
        doc: the document to encode

    Return:
        A dictionary representation of the document
    """
    encoded: dict = {}
    encoded["fullText"] = doc.text
    encoded_sentences: list = []
    for sentence in doc.sentences:

        sentiment_dict: dict = {}
        sentiment_dict["label"] = sentence.sentiment_label
        sentiment_dict["score"] = sentence.sentiment_score
        
        mood_dict: dict = {}
        mood_dict["mixedScores"] = sentence.mood
        mood_dict["score"] = sentence.mood_score
        mood_dict["label"] = sentence.mood_label

        reflection_dict: dict = {}
        reflection_dict["score"] = sentence.reflection_score
        reflection_dict["categoryScores"] = sentence.reflection

        sentence_dict: dict = {}
        sentence_dict["sentence"] = sentence.text
        sentence_dict["sentiment"] = sentiment_dict
        sentence_dict["mood"] = mood_dict
        sentence_dict["reflection"] = reflection_dict

        encoded_sentences.append(sentence_dict)
    encoded["sentences"] = encoded_sentences
    return encoded
