from flask import Flask, request, jsonify, current_app
from app.document import Document
from app.word_mappings import load_word_mappings

def analyze_document():
    body = request.get_json()
    text = body["text"]
    doc = Document(text)
    print(doc.sentences[0].indexed)
    return jsonify({'success': 'true'})

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
    # ml_model =  load_embedding_model()
    with app.app_context():
        current_app.word2index, current_app.index2word = load_word_mappings()
        # current_app.model = ml_model
    return app
