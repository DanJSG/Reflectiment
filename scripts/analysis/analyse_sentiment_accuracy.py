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

def write_error_outputs(sentences, errors, path):
    outfile = open(path, "w+")
    outfile.write("sentence,error\n")
    for i in range(len(sentences)):
        outfile.write(f"{sentences[i]},{errors[i]}\n")

def write_score_outputs(scores):
    pass

def main():
    n = 25
    analysis_types = ["Lexical", "ML", "Averaged"]
    sentences = [sentence.strip("\n") for sentence in open("./test_data/sentiment/test_x.txt", "r").readlines()]
    gold_scores = [float(score.strip("\n")) for score in open("./test_data/sentiment/test_y.txt", "r").readlines()]
    actual_scores = [[], [], []]
    absolute_errors = [[], [], []]
    squared_errors = [[], [], []]
    for i in range(n):
        response_scores = get_sentiment_scores(sentences[i])
        gold_score = gold_scores[i]
        for j in range(len(response_scores)):
            actual_scores[j].append(response_scores[j])
            absolute_errors[j].append(absolute_error(gold_score, response_scores[j]))
            squared_errors[j].append(squared_error(gold_score, response_scores[j]))
    for i in range(len(absolute_errors)):
        write_error_outputs(sentences[:n], absolute_errors[i], f"./results/sentiment/absolute_error_{analysis_types[i].lower()}.txt")
        write_error_outputs(sentences[:n], squared_errors[i], f"./results/sentiment/squared_error_{analysis_types[i].lower()}.txt")
    # for i in range(len(squared_errors)):
    #     print(f"================{analysis_types[i]}================")
    #     print("Mean Squared Error: " + str(stats.mean(squared_errors[i])))
    #     print("Max Squared Error: " + str(max(squared_errors[i])))
    #     print("Min Squared Error: " + str(min(squared_errors[i])))
    #     print("Squared Error Variance: " + str(stats.variance(squared_errors[i])))
    #     print("Squared Error Standard Deviation: " + str(stats.stdev(squared_errors[i])))
    # plt.plot(squared_errors[1])
    # plt.hlines(y=stats.mean(squared_errors[1]), xmin=0, xmax=len(squared_errors[1]), colors="r", linestyles="--")
    # plt.ylim((0, 1))
    # plt.title("ML Analysis Squared Error")
    # plt.xlabel("Sentence Index")
    # plt.ylabel("Absolute Error")
    # plt.show()

if __name__ == '__main__':
    main()