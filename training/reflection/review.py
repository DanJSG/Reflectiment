from statistics import mean

sentences_file = open("./sentences.txt", "r", encoding='utf8')
scores_file = open("scores.txt", "r", encoding='utf8')

categories = ["experience", "feelings", "personal", "perspective", "outcome", "critical"]

experience = []
feelings = []
personal = []
perspective = []
outcome = []
critical = []

means = []
for sentence_line, scores_line in zip(sentences_file.readlines(), scores_file.readlines()):
    scores = [(int(score) - 1) / 4 for score in scores_line.split(",")]

    experience.append((scores[0] * 4) + 1)
    feelings.append((scores[1] * 4) + 1)
    personal.append((scores[2] * 4) + 1)
    perspective.append((scores[3] * 4) + 1)
    outcome.append((scores[4] * 4) + 1)
    critical.append((scores[5] * 4) + 1)

    sentence = sentence_line.strip("\n")
    means.append(mean(scores))
    print(f"{sentence} = {mean(scores)}")

print("Average sentence reflection score: " + str(mean(means)))

sums = [sum(experience), sum(feelings), sum(personal), sum(perspective), sum(outcome), sum(critical)]

print(max(means))

print(categories[sums.index(max(sums))])



# experience_sum = sum(experience)
# feelings_sum = sum(feelings)
# personal_sum = sum(personal)
# perspective_sum = sum(perspective)
# outcome_sum = sum(outcome)
# critical_sum = sum(critical)


