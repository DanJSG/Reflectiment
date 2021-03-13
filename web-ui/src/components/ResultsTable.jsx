import React from 'react';

function ResultsTable(props) {

    return (
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
                    <td>{props.scores.sentiment.toFixed(5)}</td>
                    <td>{props.scores.sentimentLabel}</td>
                </tr>
                <tr>
                    <td>Mood</td>
                    <td>{props.scores.mood.toFixed(5)}</td>
                    <td>{!props.scores.moodLabel ? "N/A" : props.scores.moodLabel}</td>
                </tr>
                <tr>
                    <td>Reflection</td>
                    <td>{props.scores.reflection.toFixed(5)}</td>
                    <td>N/A</td>
                </tr>
            </tbody>
        </table>
    );

}

export default ResultsTable;
