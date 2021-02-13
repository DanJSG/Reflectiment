import app

def start():
    print("Function [start()] called.")
    service = app.get_app()
    service.run(port=8080)

if __name__ == '__main__':
    start()
