import re

def main():
    sw_file = open("./StopWords.txt", 'r')
    new_sw_file = open("./NewFile.txt", 'w+')
    regex = re.compile(" +")
    for line in sw_file.readlines():
        line = regex.sub(r"\n", line)
        new_sw_file.writelines(line)
        print(line)

if __name__ == '__main__':
    main()
