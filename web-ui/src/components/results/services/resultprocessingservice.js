/**
 * Calculates the index of the rows with the maxium values for sentiment, mood 
 * and reflection.
 * 
 * @param {Object[]} sentences 
 * @param {string} analysisTypeKey 
 * @returns the index of the rows with maximum values for sentiment, mood and reflection
 */
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
    // Loop over each sentence and its associated score, keeping track of the max values
    // and updating the index values whenever a new max is found
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

/**
 * Calculates the index of the rows with the minimum values for sentiment, mood 
 * and reflection.
 * 
 * @param {Object[]} sentences 
 * @param {string} analysisTypeKey 
 * @returns the index of the rows with minimum values for sentiment, mood and reflection
 */
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
    // Loop over each sentence and its associated score, keeping track of the max values
    // and updating the index values whenever a new minimum is found
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
