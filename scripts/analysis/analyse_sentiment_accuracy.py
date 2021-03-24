from requests import post, Response
import statistics as stats
import matplotlib.pyplot as plt

def send_api_request(sentence):
    json = {
        "text": sentence
    }
    response: Response = post("http://localhost:8080/api/v1/document", json=json)
    return response.json()

def get_sentiment_scores(sentence):
    analysis = send_api_request(sentence)
    lexical_score = (analysis["sentences"][0]["lexicalScores"]["sentiment"]["score"] + 1) / 2
    ml_score = (analysis["sentences"][0]["mlScores"]["sentiment"]["score"] + 1) / 2
    average_score = (analysis["sentences"][0]["averageScores"]["sentiment"]["score"] + 1) / 2
    return [lexical_score, ml_score, average_score]

def squared_error(gold_score, actual_score):
    return (actual_score - gold_score) ** 2

def absolute_error(gold_score, actual_score):
    return abs(actual_score - gold_score)

def main():
    analysis_types = ["Lexical", "ML", "Averaged"]
    sentences = [sentence.strip("\n") for sentence in open("./test_data/sentiment/test_x.txt", "r").readlines()]
    scores = [float(score.strip("\n")) for score in open("./test_data/sentiment/test_y.txt", "r").readlines()]
    absolute_errors = [[], [], []]
    squared_errors = [[], [], []]
    for i in range(500):
        response_scores = get_sentiment_scores(sentences[i])
        gold_score = scores[i]
        for j in range(len(response_scores)):
            absolute_errors[j].append(absolute_error(gold_score, response_scores[j]))
            squared_errors[j].append(squared_error(gold_score, response_scores[j]))
    for i in range(len(squared_errors)):
        print(f"================{analysis_types[i]}================")
        print("Mean Squared Error: " + str(stats.mean(squared_errors[i])))
        print("Max Squared Error: " + str(max(squared_errors[i])))
        print("Min Squared Error: " + str(min(squared_errors[i])))
        print("Squared Error Variance: " + str(stats.variance(squared_errors[i])))
        print("Squared Error Standard Deviation: " + str(stats.stdev(squared_errors[i])))
    plt.plot(absolute_errors[1])
    plt.hlines(y=stats.mean(absolute_errors[1]), xmin=0, xmax=len(absolute_errors[1]), colors="r", linestyles="--")
    # plt.hlines(y=max(squared_errors[1]), xmin=0, xmax=len(squared_errors[1]))
    # plt.hlines(y=min(squared_errors[1]), xmin=0, xmax=len(squared_errors[1]))
    plt.ylim((0, 1))
    plt.title("ML Analysis Squared Error")
    plt.xlabel("Sentence Index")
    plt.ylabel("Absolute Error")
    plt.show()
    # print(squared_errors)
    # response_scores = get_sentiment_scores("The movie was great!")
    # gold_score = 0.69
    # for score in response_scores:
    #     print(absolute_error(gold_score, score))
    #     print(squared_error(gold_score, score))

if __name__ == '__main__':
    main()