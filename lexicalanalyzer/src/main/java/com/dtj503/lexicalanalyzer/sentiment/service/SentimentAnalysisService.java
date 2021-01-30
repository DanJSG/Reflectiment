package com.dtj503.lexicalanalyzer.sentiment.service;

import com.dtj503.lexicalanalyzer.libs.sql.MySQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLColumn;
import com.dtj503.lexicalanalyzer.libs.sql.SQLRepository;
import com.dtj503.lexicalanalyzer.libs.sql.SQLTable;
import com.dtj503.lexicalanalyzer.parsers.DocumentParser;
import com.dtj503.lexicalanalyzer.sentiment.types.ScoreWordBuilder;
import com.dtj503.lexicalanalyzer.sentiment.types.ScoredWord;
import com.dtj503.lexicalanalyzer.types.Document;
import com.dtj503.lexicalanalyzer.types.Sentence;
import com.dtj503.lexicalanalyzer.types.Word;
import edu.stanford.nlp.util.Scored;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentAnalysisService {

    public static float analyseSentiment(String text) {
        System.out.println(text);
        Document doc = DocumentParser.parseText(text);
        for(Sentence sentence : doc.getSentences()) {

            System.out.println(sentence);

            List<Word> words = sentence.getWords();
            List<SQLColumn> cols = new ArrayList<>(words.size());
            List<String> wordStrings = new ArrayList<>(words.size());

            Map<String, Integer> posIndex = new HashMap<>();
            posIndex.put("v", 0);
            posIndex.put("n", 1);
            posIndex.put("r", 2);
            posIndex.put("a", 3);

            for(Word word : words) {
                wordStrings.add(word.getWord());
                cols.add(SQLColumn.WORD);
            }

            SQLRepository<ScoredWord> repo = new MySQLRepository<>(SQLTable.SENTIMENT);
            List<ScoredWord> scoredWords = repo.findWhereEqualOr(cols, wordStrings, 0, new ScoreWordBuilder());

            int currentWordIndex = 0;
            Map<String, List<String>> wordPosMap = new HashMap<>();
            Map<String, List<ScoredWord>> scoredWordMap = new HashMap<>();
            for(String word : wordStrings) {
                wordPosMap.put(word, new ArrayList<>());
                scoredWordMap.put(word, new ArrayList<>());
            }
            for(ScoredWord word : scoredWords) {
                wordPosMap.get(word.getWord()).add(word.getPartOfSpeech());
                scoredWordMap.get(word.getWord()).add(word);
            }

            List<ScoredWord> finalScoredWords = new ArrayList<>(words.size());
            for(Word word : words) {
                if(wordPosMap.get(word.getWord()).size() == 0) {
                    finalScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech()));
                    continue;
                }
                int index = -1;
                float wordScoreSum = 0;
                int numWords = wordPosMap.get(word.getWord()).size();
                for(int i = 0; i < numWords; i++) {
                    if(wordPosMap.get(word.getWord()).get(i).contentEquals(word.getPartOfSpeech())) {
                        index = i;
                        break;
                    }
                }
                if(index == -1) {
                    for(ScoredWord scoredWord : scoredWordMap.get(word.getWord())) {
                        wordScoreSum += scoredWord.getScore();
                    }
                    float meanScore = wordScoreSum / numWords;
                    finalScoredWords.add(new ScoredWord(word.getWord(), word.getPartOfSpeech(), meanScore));
                } else {
                    finalScoredWords.add(scoredWordMap.get(word.getWord()).get(index));
                }
            }
            for(ScoredWord word : finalScoredWords) {
                System.out.println(word);
            }

        }

        return 0;
    }

}
