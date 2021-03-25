from requests import post, Response
from timeit import default_timer as timer

def update_progress(current, total, time_per_item):
    percentage = current / total
    eta = "inf" if time_per_item == 0 else str(round((time_per_item * total) - (time_per_item * current) , 2)) + "s"
    progress_possibilities = [
        f"{current}/{total} [>                              ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=>                             ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==>                            ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===>                           ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [====>                          ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=====>                         ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [======>                        ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=======>                       ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [========>                      ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=========>                     ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==========>                    ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===========>                   ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [============>                  ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=============>                 ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==============>                ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===============>               ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [================>              ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=================>             ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==================>            ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===================>           ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [====================>          ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=====================>         ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [======================>        ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=======================>       ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [========================>      ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=========================>     ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==========================>    ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===========================>   ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [============================>  ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [=============================> ] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [==============================>] {round(percentage * 100, 4)}% -- ETA {eta}",
        f"{current}/{total} [===============================] {round(percentage * 100, 4)}% -- ETA {eta}"
    ]
    percentage_resolution = 1 / len(progress_possibilities)
    resolutions = [x * percentage_resolution for x in range(1, len(progress_possibilities) + 1)]
    prev_resolution = 0
    count = 0
    index = 0
    for val in resolutions:
        if percentage >= prev_resolution and percentage < val:
            index = count
            break
        count += 1
    print(progress_possibilities[index], end=("\r" if current != total else "\r\n"))

def send_api_request(sentence):
    json = {
        "text": sentence
    }
    response: Response = post("http://localhost:8080/api/v1/document", json=json)
    return response.json()

def get_sentiment_scores(sentence):
    analysis: dict = send_api_request(sentence)
    try:
        lexical_score = (analysis["sentences"][0]["lexicalScores"]["reflection"]["score"] + 1) / 2
        ml_score = (analysis["sentences"][0]["mlScores"]["reflection"]["score"] + 1) / 2
        average_score = (analysis["sentences"][0]["averageScores"]["reflection"]["score"] + 1) / 2
        return [lexical_score, ml_score, average_score]
    except:
        return None

def squared_error(gold_score, actual_score):
    return (actual_score - gold_score) ** 2

def absolute_error(gold_score, actual_score):
    return abs(actual_score - gold_score)

def write_error_outputs(sentences, errors, path):
    outfile = open(path, "w+")
    outfile.write("#sentence,error\n")
    for i in range(len(sentences)):
        outfile.write(f"{sentences[i]},{errors[i]}\n")

def write_score_outputs(sentences, gold_scores, actual_scores, path):
    outfile = open(path, "w+")
    outfile.write("#sentence,gold score,actual score\n")
    for i in range(len(sentences)):
        outfile.write(f"{sentences[i]},{gold_scores[i]},{actual_scores[i]}\n")

def main():
    n = 158
    analysis_types = ["Lexical", "ML", "Averaged"]
    sentences = [sentence.strip("\n") for sentence in open("./test_data/reflection/test_x.txt", "r").readlines()]
    gold_scores = [float(score.strip("\n").split(",")[0]) for score in open("./test_data/reflection/test_y.txt", "r").readlines()]
    actual_scores = [[], [], []]
    absolute_errors = [[], [], []]
    squared_errors = [[], [], []]
    times = []
    average_time = 0
    used_sentences = []
    for i in range(n):
        start = timer()
        update_progress(i, n, average_time)
        response_scores = get_sentiment_scores(sentences[i])
        if response_scores == None:
            continue
        used_sentences.append(sentences[i])
        gold_score = gold_scores[i]
        for j in range(len(response_scores)):
            actual_scores[j].append(response_scores[j])
            absolute_errors[j].append(absolute_error(gold_score, response_scores[j]))
            squared_errors[j].append(squared_error(gold_score, response_scores[j]))
        end = timer()
        times.append(end - start)
        average_time = sum(times) / len(times)
        
    for i in range(len(absolute_errors)):
        write_error_outputs(used_sentences[:n], absolute_errors[i], f"./results/reflection/overall/absolute_error_{analysis_types[i].lower()}.txt")
        write_error_outputs(used_sentences[:n], squared_errors[i], f"./results/reflection/overall/squared_error_{analysis_types[i].lower()}.txt")
        write_score_outputs(used_sentences[:n], gold_scores[:n], actual_scores[i], f"./results/reflection/overall/scores_{analysis_types[i].lower()}.txt")

if __name__ == '__main__':
    main()