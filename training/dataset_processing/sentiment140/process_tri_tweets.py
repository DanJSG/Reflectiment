import re, string, random
from nltk import word_tokenize

def get_tweet(quote_split, digits_regex, at_regex, url_regex):
    tweet = quote_split[11]
    tweet = digits_regex.sub('00', tweet)
    tweet = at_regex.sub('', tweet)
    tweet = url_regex.sub('', tweet)
    tweet = tweet.translate(str.maketrans('','', string.punctuation)).lower().strip()
    return tweet

def get_sentiment(label):
    if label == 0:
        return 0
    elif label == 2:
        return 1
    elif label == 4:
        return 2 

def get_tweet_sentiment(line, digits_regex, at_regex, url_regex):
    tweet = get_tweet(line.split("\""), digits_regex, at_regex, url_regex)
    sentiment = get_sentiment(int(line.split(",")[0].strip("\"")))
    return [tweet, sentiment]

def process_dataset():
    data_file = open("./dataset_processing/sentiment140/validationset.dataset.csv", "r")
    train_x_file = open("./processed_datasets/sentiment140/tri_validation_x.txt", "w+")
    train_y_file = open("./processed_datasets/sentiment140/tri_validation_y.txt", "w+")
    digits_regex = re.compile(r"\d+")
    at_regex = re.compile(r"@.[a-zA-Z0-9\_]*")
    url_regex = re.compile(r"http://.[a-zA-Z\.\/0-9]*|https://.[a-zA-Z\.\/0-9]*")
    pairs = [get_tweet_sentiment(line, digits_regex, at_regex, url_regex) for line in data_file.readlines()]
    random.shuffle(pairs)
    for tweet, category in pairs:
        train_x_file.write(f"{tweet}\n")
        train_y_file.write(f"{category}\n")

if __name__ == '__main__':
    process_dataset()
