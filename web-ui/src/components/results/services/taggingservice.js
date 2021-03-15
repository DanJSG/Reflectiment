const tagSentiment = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: colorVal > 0 ? `rgba(0, 255, 0, ${Math.abs(colorVal)})` : `rgb(255, 0, 0, ${Math.abs(colorVal)})`
    }
    return <span key={index} style={colorStyle}>{sentence}&nbsp;</span>;
}

const tagMood = (sentence, scores, index) => {
    console.log(scores.mixedScores);
    let colorStyle = {
        backgroundColor: `rgba(0, 0, 0, 0)`,
        color: "#212529"
    };
    if(scores.label === "anger") {
        colorStyle.backgroundColor = `rgba(255, 0, 0, ${scores.score})`;
    } else if(scores.label === "sadness") {
        colorStyle.backgroundColor = `rgba(0, 0, 255, ${scores.score})`;
        colorStyle.color = scores.score > 0.5 ? "#f8f9fa" : colorStyle.color;
    } else if(scores.label === "fear") {
        colorStyle.backgroundColor = `rgba(128, 0, 128, ${scores.score})`;
        colorStyle.color = scores.score > 0.5 ? "#f8f9fa" : colorStyle.color;
    } else {
        colorStyle.backgroundColor = `rgba(255, 255, 0, ${scores.score})`;
    }
    return (
        <span key={index} style={colorStyle}>{sentence}&nbsp;</span>
    )
}

const tagReflection = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: `rgba(${128 - (colorVal * 95)}, ${128 + (colorVal * 95)}, 0, ${colorVal})`
    }
    return <span key={index} style={colorStyle}>{sentence}&nbsp;</span>;
}

export const pickTaggingFunction = (analysisFeature) => {
    if(analysisFeature === "sentiment") {
        return tagSentiment;
    } else if(analysisFeature === "mood") {
        return tagMood;
    } else {
        return tagReflection;
    }
}
