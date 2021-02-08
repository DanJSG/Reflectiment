import mysql.connector

def main():

    db = mysql.connector.connect(
        host="localhost",
        user="root",
        password="password",
        database="lexicalanalyzer"
    )

    cursor = db.cursor()

    sql = "INSERT INTO reflection (word, pos, category, tag, score) VALUES (%s, %s, %s, %s, %s)"

    lexicon_file = open("./scripts/Reflection Lexicon/output.txt", "r")

    pos_dict = {
        "v": 0,
        "n": 1,
        "r": 2,
        "a": 3,
        "c": 4
    }

    count = 0
    for line in lexicon_file.readlines():
        if line.startswith("#"):
            continue
        
        props = line.split("\t")

        print(props)

        word = props[0]
        pos = pos_dict[props[1]]
        category = props[2]
        score = float(props[3].strip("\n"))

        sql_values = (word, pos, category, "ullman", score)
        cursor.execute(sql, sql_values)

        if count == 250:
            db.commit()
            count = 0
        else:
            count = count + 1
    db.commit()

if __name__ == '__main__':
    main()
