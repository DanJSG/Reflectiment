import re
import mysql.connector

def main():

    db = mysql.connector.connect(
        host="localhost",
        user="root",
        password="password",
        database="lexicalanalyzer"
    )
    cursor = db.cursor()

    sql = "INSERT INTO sentiment (tag, pos, word, sentiment) VALUES (%s, %s, %s, %s)"

    sw_file = open("./SentiWords.txt", "r")

    regex_matches = [re.compile("#v"), re.compile("#n"), re.compile("#r"), re.compile("#a")]

    tag = "sentiwords"
    count = 0
    for line in sw_file.readlines():
        if line[0] == "#":
            continue
        index = -1
        for n in range(0, len(regex_matches)):
            match = regex_matches[n].search(line)
            if match != None:
                index = n
                break
        pos = index
        word = line[0:match.span()[0]]
        sentiment = float(line[match.span()[1]:len(line)].strip())
        if index != 0 and index != 1 and index != 2 and index != 3:
            print("Error matching")
            continue
        sql_values = (tag, pos, word, sentiment)
        cursor.execute(sql, sql_values)
        if count == 250:
            db.commit()
            count = 0
        else:
            count = count + 1
    db.commit()

if __name__ == '__main__':
    main()
