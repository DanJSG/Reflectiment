import app

def start() -> None:
    """ Initialize and start the Flask web application."""
    print("Function [start()] called.")
    service = app.get_app()
    service.run(host='0.0.0.0', port=8080, use_reloader=False)

if __name__ == '__main__':
    start()
