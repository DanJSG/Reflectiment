import os.path

if os.path.isfile("./progress.txt"):
    progress_file = open("./progress.txt", "r")
    start_pos = int(progress_file.readlines()[0])
else:
    start_pos = 0

sentences_file = open("./sentences.txt", "r", encoding='utf8')
score_file = open("./scores.txt", "a+")

count = 0
processed = 0

for sentence in sentences_file.readlines():
    if count < start_pos:
        count += 1
        continue
    print("\n")
    print(f"{sentence}")
    experience = input("Experience: ")
    feelings = input("Feelings: ")
    personal = input("Personal Writing: ")
    perspective = input("Perspective: ")
    outcome = input("Outcome: ")
    critical = input("Critical Stance: ")
    score_file.write(f"{experience},{feelings},{personal},{perspective},{outcome},{critical}\n")
    count += 1
    processed += 1
    cont = input("Continue? (y/n)")
    if cont.lower() == 'n':
        break

progress_file = open("./progress.txt", "w+")
progress_file.write(f"{start_pos + processed}\n")
