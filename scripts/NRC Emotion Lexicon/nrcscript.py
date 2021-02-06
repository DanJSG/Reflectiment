import mysql.connector

def main():

    db = mysql.connector.connect(
        host="localhost",
        user="root",
        password="password",
        database="lexicalanalyzer"
    )
    cursor = db.cursor()

    sql = "INSERT INTO mood (word, emotion, tag, score) VALUES (%s, %s, %s, %s)"

    nrc_file = open("./scripts/NRC Emotion Lexicon/NRCv1.txt", "r")
    tag = "nrc"

    count = 0
    for line in nrc_file.readlines():
        splitline = line.split("\t")
        splitline[2] = float(splitline[2].strip("\n"))
        if splitline[1] == "trust" or splitline[1] == "disgust" or splitline[1] == "anticipation" or splitline[1] == "surprise":
            continue
        sql_values = (splitline[0], splitline[1], tag, splitline[2])
        cursor.execute(sql, sql_values)
        if count == 250:
            db.commit()
            count = 0
        else:
            count = count + 1
    db.commit()
    db.close()


if __name__ == '__main__':
    main()