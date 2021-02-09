package com.dtj503.lexicalanalyzer.reflection.service;

import com.dtj503.lexicalanalyzer.common.parsers.StringParser;
import com.dtj503.lexicalanalyzer.common.services.AnalysisService;
import com.dtj503.lexicalanalyzer.common.types.Document;
import com.dtj503.lexicalanalyzer.common.types.Token;

public class ReflectionAnalysisService extends AnalysisService {

    public static void analyseReflection(String text) {
        System.out.println("In reflection analysis service!");
        System.out.println(text);

        Document<Token> doc = StringParser.parseText(text);
        

    }

}
