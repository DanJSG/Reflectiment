from nltk.corpus import wordnet as wn

def main():

    non_seeds_file = open("./scripts/Reflection Lexicon/nonseeds.txt", "r")
    seeds_file = open("./scripts/Reflection Lexicon/seeds.txt", "r")
    output_file = open("./scripts/Reflection Lexicon/output.txt", "w+")

    output_file.writelines(non_seeds_file.readlines())

    added_word_list = []

    for line in seeds_file.readlines():

        if line.startswith("#"):
            continue

        props = line.split("\t")
        word = props[0]
        pos = props[1]
        category = props[2]
        score = float(props[3].strip("\n"))

        added_word_list.append({word, pos, category})

        output_file.write(word + "\t" + pos + "\t" + category + "\t" + str(score) + "\n")

        synonyms = wn.synsets(word)
        for synonym in synonyms:
            if synonym.pos() != pos or synonym.lemmas()[0].name() == word:
                continue
            if {synonym.lemmas()[0].name(), synonym.pos(), category} in added_word_list:
                print("Matching existing word!")
                continue
            added_word_list.append({synonym.lemmas()[0].name(), synonym.pos(), category})
            output_file.write(synonym.lemmas()[0].name() + "\t" + synonym.pos() + "\t" + category + "\t" + str(score) + "\n")

if __name__ == '__main__':
    main()
