from flask import Flask, request, jsonify, current_app
from app.document import Document
from app.embedding_model_provider import load_embedding_model

def analyze_document():
    body = request.get_json()
    text = body["text"]
    doc = Document(text)
    for sentence in doc.sentences:
        print(sentence.vectors)
    return jsonify({'success': 'true'})

def get_app():
    app = Flask(__name__)
    app.config["DEBUG"] = True
    app.add_url_rule('/api/v1/document', 'analyze_document', analyze_document, methods=["POST"])
    ml_model =  load_embedding_model()
    with app.app_context():
        current_app.model = ml_model
    return app
