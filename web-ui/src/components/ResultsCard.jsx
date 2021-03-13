import React, { useEffect, useState } from 'react';

function ResultsCard(props) {

    const [averageScores, setAverageScores] = useState(null);
    const [maxScores, setMaxScores] = useState(null);
    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [activeTab, setActiveTab] = useState(0);

    // TODO refactor into separate service class
    const getAverageScores = (sentences, analysisTypeKey) => {
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
            sentiment: sentimentSum / (parseInt(i) + 1),
            sentimentLabel: sentimentLabel,
            mood: moodSum / (parseInt(i) + 1),
            reflection: reflectionSum / (parseInt(i) + 1)
        }
    }

    // TODO refactor into separate service class
    const getMaxScores = (sentences, analysisTypeKey) => {
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

    // TODO refactor into separate service class
    const getSentimentLabel = (score) => {
        if(score >= -1 && score < -0.33) {
            return "Negative";
        } else if(score >= -0.33 && score < 0.33) {
            return "Neutral";
        } else {
            return "Positive";
        }
    }

    const switchTab = (e) => {
        e.preventDefault();
        const tabId = e.nativeEvent.submitter.id;
        e.target.elements[activeTab].classList.toggle("active");
        e.target.elements[tabId].classList.toggle("active");
        setActiveTab(tabId)
        showResults(analysisTypeKeys[tabId]);
    }

    const showResults = (analysisTypeKey) => {
        const averages = getAverageScores(props.analysis.sentences, analysisTypeKey);
        const maxes = getMaxScores(props.analysis.sentences, analysisTypeKey);
        setAverageScores(averages);
        setMaxScores(maxes);
    }

    useEffect(() => {
        if(!props.analysis) {
            return;
        }
        showResults(analysisTypeKeys[0]);
    }, [props.analysis, analysisTypeKeys])

    return (
        <div className="card w-100 shadow-sm border-1 row">
            <div className="card-header">
                <h1 className="font-weight-normal">Results</h1>
                    <form onSubmit={switchTab}>
                        <ul className="nav nav-tabs card-header-tabs">
                            <li className="nav-item">
                                <button className="btn-link nav-link active" id={0}>Lexical</button>
                            </li>
                            <li className="nav-item">
                                <button className="btn-link nav-link" id={1}>Machine Learning</button>
                            </li>
                            <li className="nav-item">
                                <button className="btn-link nav-link" id={2}>Combined</button>
                            </li>
                        </ul>
                    </form>
            </div>
            <div className="container-fluid p-3">
                <div className="row">
                    <div className="col-6 border-right">
                        <h3 className="font-weight-normal text-center card-title">Average Scores</h3>
                        {!averageScores ? null : 
                            // TODO Refactor table into another component
                            <table className="table">
                                <thead>
                                    <tr>
                                        <th scope="col" style={{width: "33%"}}>Aspect</th>
                                        <th className="col" style={{width: "33%"}}>Intensity</th>
                                        <th className="col" style={{width: "33%"}}>Label</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Sentiment</td>
                                        <td>{averageScores.sentiment.toFixed(5)}</td>
                                        <td>{averageScores.sentimentLabel}</td>
                                    </tr>
                                    <tr>
                                        <td>Mood</td>
                                        <td>{averageScores.mood.toFixed(5)}</td>
                                        <td>N/A</td>
                                    </tr>
                                    <tr>
                                        <td>Reflection</td>
                                        <td>{averageScores.reflection.toFixed(5)}</td>
                                        <td>N/A</td>
                                    </tr>
                                </tbody>
                            </table>
                        }
                    </div>
                    <div className="col-6">
                        <h3 className="font-weight-normal text-center card-title">Maximum Scores</h3>
                        {!maxScores ? null : 
                            // TODO Refactor table into another component
                            <table className="table">
                                <thead>
                                    <tr>
                                        <th scope="col" style={{width: "33%"}}>Aspect</th>
                                        <th className="col" style={{width: "33%"}}>Intensity</th>
                                        <th className="col" style={{width: "33%"}}>Label</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Sentiment</td>
                                        <td>{maxScores.sentiment.toFixed(5)}</td>
                                        <td>{maxScores.sentimentLabel}</td>
                                    </tr>
                                    <tr>
                                        <td>Mood</td>
                                        <td>{maxScores.mood.toFixed(5)}</td>
                                        <td>{maxScores.moodLabel}</td>
                                    </tr>
                                    <tr>
                                        <td>Reflection</td>
                                        <td>{maxScores.reflection.toFixed(5)}</td>
                                        <td>N/A</td>
                                    </tr>
                                </tbody>
                            </table>
                        }
                    </div>
                </div>
                <hr/>
                <div className="row p-3">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Tagged Text</h2>
                        {!props.analysis ? null :
                            <div>
                                <p>{props.analysis.text}</p>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;
