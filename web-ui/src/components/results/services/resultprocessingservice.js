export const getAverageScores = (sentences, analysisTypeKey) => {
    let sentimentSum = 0;
    let moodSum = 0;
    let sentimentLabel = "Neutral";
    let reflectionSum = 0;
    let i = 0;
    for(i; i < sentences.length; i++) {
        sentimentSum += sentences[i][analysisTypeKey].sentiment.score;
        moodSum += sentences[i][analysisTypeKey].mood.score;
        reflectionSum += sentences[i][analysisTypeKey].reflection.score;
    }
    sentimentLabel = getSentimentLabel(sentimentSum / (parseInt(i) + 1));
    return {
        sentiment: sentimentSum / (parseInt(i)),
        sentimentLabel: sentimentLabel,
        mood: moodSum / (parseInt(i)),
        reflection: reflectionSum / (parseInt(i))
    }
}

export const getMinScores = (sentences, analysisTypeKey) => {
    let mins = {
        sentiment: 1,
        sentimentLabel: "Neutral",
        mood: 1,
        moodLabel: "None",
        reflection: 1
    }
    for(let i = 0; i < sentences.length; i++) {
        mins.sentiment = sentences[i][analysisTypeKey].sentiment.score < mins.sentiment ? sentences[i][analysisTypeKey].sentiment.score : mins.sentiment;
        mins.moodLabel = sentences[i][analysisTypeKey].mood.score < mins.mood ? sentences[i][analysisTypeKey].mood.label : mins.moodLabel;
        mins.moodLabel = mins.moodLabel.charAt(0).toUpperCase() + mins.moodLabel.substr(1);
        mins.mood = sentences[i][analysisTypeKey].mood.score < mins.mood ? sentences[i][analysisTypeKey].mood.score : mins.mood;
        mins.reflection = sentences[i][analysisTypeKey].reflection.score < mins.reflection ? sentences[i][analysisTypeKey].reflection.score : mins.reflection;
    }
    mins.sentimentLabel = getSentimentLabel(mins.sentiment);
    console.log(mins);
    return mins;
}

export const getMaxScores = (sentences, analysisTypeKey) => {
    let maxes = {
        sentiment: -1,
        sentimentLabel: "Neutral",
        mood: 0,
        moodLabel: "None",
        reflection: 0
    }
    for(let i = 0; i < sentences.length; i++) {
        maxes.sentiment = sentences[i][analysisTypeKey].sentiment.score > maxes.sentiment ? sentences[i][analysisTypeKey].sentiment.score : maxes.sentiment;
        maxes.moodLabel = sentences[i][analysisTypeKey].mood.score > maxes.mood ? sentences[i][analysisTypeKey].mood.label : maxes.moodLabel;
        maxes.moodLabel = maxes.moodLabel.charAt(0).toUpperCase() + maxes.moodLabel.substr(1);
        maxes.mood = sentences[i][analysisTypeKey].mood.score > maxes.mood ? sentences[i][analysisTypeKey].mood.score : maxes.mood;
        maxes.reflection = sentences[i][analysisTypeKey].reflection.score > maxes.reflection ? sentences[i][analysisTypeKey].reflection.score : maxes.reflection;
    }
    maxes.sentimentLabel = getSentimentLabel(maxes.sentiment);
    return maxes;
}

const getSentimentLabel = (score) => {
    if(score >= -1 && score < -0.33) {
        return "Negative";
    } else if(score >= -0.33 && score < 0.33) {
        return "Neutral";
    } else {
        return "Positive";
    }
}
