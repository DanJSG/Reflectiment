/**
 * Generates a downloadable CSV file for the sentiment results table.
 * 
 * @param {string[]} sentences 
 * @param {Object[]} scores 
 * @param {string} analysisTypeKey 
 * @param {string} analysisFeature 
 * @returns a downloadable CSV file blob
 */
export const generateSentimentCsv = (sentences, scores) => {
    const dataRows = [["Sentence", "Intensity", "Label"]];
    for(let i = 0; i < sentences.length; i++) {
        dataRows.push([`"${sentences[i]}"`, String(scores[i].score), `"${scores[i].label}"`]);
    }
    const blob = new Blob(dataRows.map(row => String(row) + "\n"), {type: "text/csv"});
    return blob;
}

/**
 * Generates a downloadable CSV file for the mood results table.
 * 
 * @param {string[]} sentences 
 * @param {Object[]} scores 
 * @param {string} analysisTypeKey 
 * @param {string} analysisFeature 
 * @returns a downloadable CSV file blob
 */
export const generateMoodCsv = (sentences, scores) => {
    const dataRows = [["Sentence", "Joy", "Anger", "Fear", "Sadness"]];
    for(let i = 0; i < sentences.length; i++) {
        const joyScore = String(scores[i].mixedScores["joy"]);
        const angerScore = String(scores[i].mixedScores["anger"]);
        const fearScore = String(scores[i].mixedScores["fear"]);
        const sadnessScore = String(scores[i].mixedScores["sadness"]);
        dataRows.push([`"${sentences[i]}"`, joyScore, angerScore, fearScore, sadnessScore]);
    }
    const blob = new Blob(dataRows.map(row => String(row) + "\n"), {type: "text/csv"});
    return blob;
}

/**
 * Generates a downloadable CSV file for the reflection results table.
 * 
 * @param {string[]} sentences 
 * @param {Object[]} scores 
 * @param {string} analysisTypeKey 
 * @param {string} analysisFeature 
 * @returns a downloadable CSV file blob
 */
export const generateReflectionCsv = (sentences, scores) => {
    const dataRows = [["Sentence", "Overall"]];
    let features;
    if(scores[0].categoryScores) {
        features = Object.keys(scores[0].categoryScores).map(key => key);
        dataRows[0] = dataRows[0].concat(features.map(feature => feature.charAt(0).toUpperCase() + feature.substr(1)));
    }
    for(let i = 0; i < sentences.length; i++) {
        let row = [`"${sentences[i]}"`, String(scores[i].score)];
        if(scores[0].categoryScores) {
            let featureIndex = [];
            Object.entries(scores[i].categoryScores).forEach(tuple => featureIndex.push(features.indexOf(tuple[0])));
            const featureScores = featureIndex.map(index => scores[i].categoryScores[features[index]]);
            row = row.concat(featureScores.map(score => String(score)));
        }
        dataRows.push(row);
    }
    const blob = new Blob(dataRows.map(row => String(row) + "\n"), {type: "text/csv"});
    return blob;
}
