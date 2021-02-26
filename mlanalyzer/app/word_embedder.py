import keras
import tensorflow as tf

class WordEmbedder():
    def __init__(self) -> None:
        self._json_path = "./models/embedding/embedding.json"
        self._weights_path = "./models/embedding/embedding.hdf5"
        self._configure_gpu()
        self._model = self._load_model()
        self._dummy_request()

    def get_embeddings(self, tokenized_sentence) -> list:
        return self._model.predict(tokenized_sentence).tolist()

    def _dummy_request(self) -> None:
        self._model.predict([[150]])

    def _load_model(self) -> keras.Sequential:
        """ Load the machine learning model from the JSON and hdf5 files.
        Returns:
            A loaded and initialized Keras model
        """
        print("Loading model...")
        model: keras.Sequential = keras.models.model_from_json(open(self._json_path, "r").read())
        model.load_weights(self._weights_path)
        print("Model loaded.")
        model.summary()
        return model

    @staticmethod
    def _configure_gpu() -> None:
        """ Initialize GPU memory growth to optimize performance."""
        physical_devices = tf.config.experimental.list_physical_devices('GPU')
        tf.config.experimental.set_memory_growth(physical_devices[0], True)
