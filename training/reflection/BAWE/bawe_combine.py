import os

outfile = open("./BAWE/combined/combined.txt", "w+", encoding="utf8")

dir = "./BAWE/individual/"

for filename in os.listdir(dir):
    curr_file = open(os.path.join(dir, filename), "r", encoding="utf8")
    outfile.write(f"{curr_file.read()}\n")
