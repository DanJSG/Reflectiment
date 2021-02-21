from app.document_encoder import encode_document
from app.sentiment_analyzer import SentimentAnalyzer
from flask import Flask, request, jsonify, current_app
from app.document import Document
from app.word_mappings import load_word_mappings

def analyze_document():
    body: dict = request.get_json()
    text: str = body["text"]
    doc: Document = Document(text)
    return jsonify(encode_document(doc))

def get_app() -> Flask:
    """ Initialize and fetch the Flask application.

    Initialize the Flask application by setting the app state and adding its endpoints. This 
    method does not start the Flask application.

    Returns:
        An initialized Flask application
    """
    app = Flask(__name__)
    app.config["DEBUG"] = True
    app.add_url_rule('/api/v1/document', 'analyze_document', analyze_document, methods=["POST"])
    with app.app_context():
        current_app.word2index, current_app.index2word = load_word_mappings()
        current_app.sentiment_analyzer = SentimentAnalyzer()
    return app
