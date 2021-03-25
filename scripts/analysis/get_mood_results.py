from requests import Response, post

def send_api_request(sentence):
    json = {
        "text": sentence
    }
    response: Response = post("http://localhost:8080/api/v1/document", json=json)
    return response.json()

def get_mood_scores(sentence):
    score_types = ["lexicalScores", "mlScores", "averageScores"]
    moods = ["joy", "anger", "fear", "sadness"]
    try:
        analysis = send_api_request(sentence)
        scores = {
            "joy": [],
            "anger": [],
            "fear": [],
            "sadness": []
        }
        for score_type in score_types:
            mixed_scores = analysis["sentences"][0][score_type]["mood"]["mixedScores"]
            for mood in moods:
                scores[mood].append(mixed_scores[mood])
        return scores
    except:
        return None

def init_stats_dict():
    return {
        "joy": [[], [], []],
        "anger": [[], [], []],
        "fear": [[], [], []],
        "sadness": [[], [], []]
    }

def squared_error(gold_score, actual_score):
    return (actual_score - gold_score) ** 2

def absolute_error(gold_score, actual_score):
    return abs(actual_score - gold_score)

def write_error_outputs(sentences, errors, path):
    outfile = open(path, "w+", encoding="utf8")
    outfile.write("#sentence,error\n")
    for i in range(len(sentences)):
        print(i)
        outfile.write(f"{sentences[i]},{errors[i]}\n")
  
def write_score_outputs(sentences, gold_scores, actual_scores, path):
    outfile = open(path, "w+", encoding="utf8")
    outfile.write("#sentence,gold score,actual score\n")
    for i in range(len(sentences)):
        outfile.write(f"{sentences[i]},{gold_scores[i]},{actual_scores[i]}\n")

def main():
    n = 3092
    moods = ["joy", "anger", "fear", "sadness"]
    analysis_types = ["Lexical", "ML", "Averaged"]
    sentences = [sentence.strip("\n") for sentence in open("./test_data/mood/test_x.txt", "r", encoding="utf8").readlines()]
    gold_scores_arr = [[float(score) for score in scores.strip("\n").split(",")] for scores in open("./test_data/mood/test_y.txt", "r").readlines()]
    gold_scores_dict = {
        "joy": [gold_scores_group[0] for gold_scores_group in gold_scores_arr],
        "anger": [gold_scores_group[1] for gold_scores_group in gold_scores_arr],
        "fear": [gold_scores_group[2] for gold_scores_group in gold_scores_arr],
        "sadness": [gold_scores_group[3] for gold_scores_group in gold_scores_arr]
    }
    actual_scores = init_stats_dict()
    absolute_errors = init_stats_dict()
    squared_errors = init_stats_dict()
    used_sentences = []
    for i in range(n):
        print(f"Sentence {i} out of {n}")
        response_scores = get_mood_scores(sentences[i])
        if response_scores == None:
            continue
        used_sentences.append(sentences[i])
        for mood in moods:
            gold_score = gold_scores_dict[mood][i]
            for j in range(len(response_scores[mood])):
                actual_scores[mood][j].append(response_scores[mood][j])
                absolute_errors[mood][j].append(absolute_error(gold_score, response_scores[mood][j]))
                squared_errors[mood][j].append(squared_error(gold_score, response_scores[mood][j]))
    
    for mood in moods:
        for i in range(len(analysis_types)):
            write_error_outputs(used_sentences[:n], absolute_errors[mood][i], f"./results/mood/{mood}/absolute_error_{analysis_types[i].lower()}.txt")
            write_error_outputs(used_sentences[:n], squared_errors[mood][i], f"./results/mood/{mood}/squared_error_{analysis_types[i].lower()}.txt")
            write_score_outputs(used_sentences[:n], gold_scores_dict[mood][:n], actual_scores[mood][i], f"./results/mood/{mood}/scores_{analysis_types[i].lower()}.txt")

if __name__ == '__main__':
    main()