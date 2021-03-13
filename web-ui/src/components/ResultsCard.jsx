import React, { useEffect, useState } from 'react';

function ResultsCard(props) {

    const [averageScores, setAverageScores] = useState(null);
    const [maxScores, setMaxScores] = useState(null);

    // TODO refactor into separate service class
    const getAverageScores = (sentences) => {
        let sentimentSum = 0;
        let moodSum = 0;
        let sentimentLabel = "Neutral";
        let reflectionSum = 0;
        let i = 0;
        for(i; i < sentences.length; i++) {
            sentimentSum += sentences[i].lexicalScores.sentiment.score;
            moodSum += sentences[i].lexicalScores.mood.score;
            reflectionSum += sentences[i].lexicalScores.reflection.score;
        }
        if(sentimentSum / (parseInt(i) + 1) >= -1 && sentimentSum / (parseInt(i) + 1) < -0.33) {
            sentimentLabel = "Negative";
        } else if(sentimentSum / (parseInt(i) + 1) >= 0.33) {
            sentimentLabel = "Positive";
        }
        return {
            sentiment: sentimentSum / (parseInt(i) + 1),
            sentimentLabel: sentimentLabel,
            mood: moodSum / (parseInt(i) + 1),
            reflection: reflectionSum / (parseInt(i) + 1)
        }
    }

    // TODO refactor into separate service class
    const getMaxScores = (sentences) => {
        let maxes = {
            sentiment: -1,
            sentimentLabel: "Neutral",
            mood: 0,
            moodLabel: "None",
            reflection: 0
        }
        for(let i = 0; i < sentences.length; i++) {
            maxes.sentiment = sentences[i].lexicalScores.sentiment.score > maxes.sentiment ? sentences[i].lexicalScores.sentiment.score : maxes.sentiment;
            maxes.moodLabel = sentences[i].lexicalScores.mood.score > maxes.mood ? sentences[i].lexicalScores.mood.label : maxes.moodLabel;
            maxes.moodLabel = maxes.moodLabel.charAt(0).toUpperCase() + maxes.moodLabel.substr(1);
            maxes.mood = sentences[i].lexicalScores.mood.score > maxes.mood ? sentences[i].lexicalScores.mood.score : maxes.mood;
            maxes.reflection = sentences[i].lexicalScores.reflection.score > maxes.reflection ? sentences[i].lexicalScores.reflection.score : maxes.reflection;
        }
        if(maxes.sentiment >= -1 && maxes.sentiment < -0.33) {
            maxes.sentimentLabel = "Negative";
        } else if(maxes.sentiment >= 0.33) {
            maxes.sentimentLabel = "Positive";
        }
        return maxes;
    }

    useEffect(() => {
        if(!props.analysis) {
            return;
        }
        const averages = getAverageScores(props.analysis.sentences);
        const maxes = getMaxScores(props.analysis.sentences);
        console.log(averages);
        setAverageScores(averages);
        setMaxScores(maxes);
    }, [props.analysis])

    return (
        <div className="card w-100 shadow-sm border-1 row">
            <div className="card-header">
                <h1 className="font-weight-normal">Results</h1>
                {/* <ul className="nav nav-tabs card-header-tabs">
                    <li className="nav-item">
                        <a className="nav-link" href="">Tab 1</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="">Tab 2</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" href="">Tab 3</a>
                    </li>
                </ul> */}
            </div>
            <div className="container-fluid p-3">
                <div className="row">
                    <div className="col-6 border-right">
                        <h3 className="font-weight-normal text-center card-title">Average Scores</h3>
                        {!averageScores ? null : 
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
