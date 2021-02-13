import app

def start():
    print("Function [start()] called.")
    service = app.get_app()
    service.run(host='0.0.0.0', port=8080, use_reloader=False)

if __name__ == '__main__':
    start()
