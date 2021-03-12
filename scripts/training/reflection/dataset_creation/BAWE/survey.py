from math import floor

file_lines = open("./BAWE/scores.txt", "r").readlines()
sentences_lines = open("./BAWE/sentences/sentences.txt", "r", encoding="utf8").readlines()

labels = ["experience", "feelings", "personal", "perspective", "outcome", "critical"]
scores = {label: [] for label in labels}

sums = []

for line in file_lines:
    line_scores = [float(score) for score in line.strip("\n").split(",")]
    sums.append(sum(line_scores))
    for i in range(len(line_scores)):
        scores[labels[i]].append(line_scores[i])

category_lines = {}
line_set = set()
line_set.add(sums.index(min(sums)))
line_set.add(sums.index(max(sums)))

for label in labels:
    max_index = scores[label].index(max(scores[label]))
    mid_index = scores[label].index(sorted(scores[label])[floor(len(scores[label]) / 2)])
    min_index = scores[label].index(min(scores[label]))
    category_lines[label] = (max_index, mid_index, min_index)

print(category_lines)

for lines in category_lines.values():
    for a in lines:
        line_set.add(a)

outfile = open("./BAWE/survey.txt", "w+", encoding="utf8")
for line_num in line_set:
    sent_nnl = sentences_lines[line_num].strip('\n')
    outfile.write(f"{sent_nnl}:{file_lines[line_num]}")

print(line_set)

