export const getMaxScoreIndexes = (sentences, analysisTypeKey) => {
    let maxes = {
        sentiment: -1,
        mood: 0,
        reflection: 0
    }
    let maxIndexes = {
        sentiment: -1,
        mood: -1,
        reflection: -1
    }
    for(let i = 0; i < sentences.length; i++) {
        if(sentences[i][analysisTypeKey].sentiment.score > maxes.sentiment) {
            maxes.sentiment = sentences[i][analysisTypeKey].sentiment.score;
            maxIndexes.sentiment = i;
        }
        if(sentences[i][analysisTypeKey].mood.score > maxes.mood) {
            maxes.mood = sentences[i][analysisTypeKey].mood.score;
            maxIndexes.mood = i;
        }
        if(sentences[i][analysisTypeKey].reflection.score > maxes.reflection) {
            maxes.reflection = sentences[i][analysisTypeKey].reflection.score;
            maxIndexes.reflection = i;
        }
    }
    return maxIndexes;
}

export const getMinScoreIndexes = (sentences, analysisTypeKey) => {
    let maxes = {
        sentiment: 1,
        mood: 1,
        reflection: 1
    }
    let maxIndexes = {
        sentiment: -1,
        mood: -1,
        reflection: -1
    }
    for(let i = 0; i < sentences.length; i++) {
        if(sentences[i][analysisTypeKey].sentiment.score < maxes.sentiment) {
            maxes.sentiment = sentences[i][analysisTypeKey].sentiment.score;
            maxIndexes.sentiment = i;
        }
        if(sentences[i][analysisTypeKey].mood.score < maxes.mood) {
            maxes.mood = sentences[i][analysisTypeKey].mood.score;
            maxIndexes.mood = i;
        }
        if(sentences[i][analysisTypeKey].reflection.score < maxes.reflection) {
            maxes.reflection = sentences[i][analysisTypeKey].reflection.score;
            maxIndexes.reflection = i;
        }
    }
    return maxIndexes;
}
