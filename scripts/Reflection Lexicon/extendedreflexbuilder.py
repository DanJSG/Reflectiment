from nltk.corpus import wordnet as wn

def main():

    non_seeds_file = open("./scripts/Reflection Lexicon/nonseeds.txt", "r")
    seeds_file = open("./scripts/Reflection Lexicon/seeds.txt", "r")
    output_file = open("./scripts/Reflection Lexicon/output_extended.txt", "w+")

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

        shifted_score = score - 0.1

        synonyms = wn.synsets(word)
        for synonym in synonyms:

            if synonym.pos() != pos or synonym.lemmas()[0].name() == word:
                continue
            if {synonym.lemmas()[0].name(), synonym.pos(), category} in added_word_list:
                print("Matching existing word! (synonym)")
                continue
            added_word_list.append({synonym.lemmas()[0].name(), synonym.pos(), category})
            output_file.write(synonym.lemmas()[0].name() + "\t" + synonym.pos() + "\t" + category + "\t" + str(round(shifted_score * 0.75, 4)) + "\n")

            hyponyms = synonym.hyponyms()
            for hyponym in hyponyms:
                # print(hyponym.pos() + ", " + hyponym.lemmas()[0].name() + ", " + category)

                if hyponym.pos() != pos or hyponym.lemmas()[0].name() == word or "_" in hyponym.lemmas()[0].name():
                    continue
                if {hyponym.lemmas()[0].name(), hyponym.pos(), category} in added_word_list:
                    print("Matching existing word! (hyponym)")
                    continue
                added_word_list.append({hyponym.lemmas()[0].name(), hyponym.pos(), category})

                output_file.write(hyponym.lemmas()[0].name() + "\t" + hyponym.pos() + "\t" + category + "\t" + str(round(shifted_score * 0.625 * 0.75, 4)) + "\n")


        hyponyms = synonyms[0].hyponyms()
        for hyponym in hyponyms:
                # print(hyponym.pos() + ", " + hyponym.lemmas()[0].name() + ", " + category)

                if hyponym.pos() != pos or hyponym.lemmas()[0].name() == word or "_" in hyponym.lemmas()[0].name():
                    continue
                if {hyponym.lemmas()[0].name(), hyponym.pos(), category} in added_word_list:
                    print("Matching existing word! (hyponym)")
                    continue
                added_word_list.append({hyponym.lemmas()[0].name(), hyponym.pos(), category})

                output_file.write(hyponym.lemmas()[0].name() + "\t" + hyponym.pos() + "\t" + category + "\t" + str(round(shifted_score * 0.625, 4)) + "\n")
        
        hypernyms = synonyms[0].hypernyms()
        for hypernym in hypernyms:
            if hypernym.pos() != pos or hypernym.lemmas()[0].name() == word or "_" in hypernym.lemmas()[0].name():
                    continue
            if {hypernym.lemmas()[0].name(), hypernym.pos(), category} in added_word_list:
                print("Matching existing word! (hypernym)")
                continue
            added_word_list.append({hypernym.lemmas()[0].name(), hypernym.pos(), category})

            output_file.write(hypernym.lemmas()[0].name() + "\t" + hypernym.pos() + "\t" + category + "\t" + str(round(shifted_score * 0.875, 4)) + "\n")
            # if len(hyponyms) > 0:
            #     print(synonym.lemmas()[0].name() + ": ")
            #     print(hyponyms)
            
if __name__ == '__main__':
    main()
