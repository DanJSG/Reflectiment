import React, { useState } from 'react';
import {getRowStyle} from './services/rowstyler';

function MoodSentenceTable(props) {

    const [columnWidth] = useState("16.67%");

    const fillTable = () => {
        console.log(props.sentences[0][props.analysisTypeKey]["mood"].mixedScores["joy"]);
        return props.sentences.map((sentence, index) => {
            return (
                <tr key={index} style={getRowStyle(index, props.maxIndex, props.minIndex)}>
                    <td>{sentence.sentence}</td>
                    <td>{sentence[props.analysisTypeKey]["mood"].label.charAt(0).toUpperCase() + sentence[props.analysisTypeKey]["mood"].label.substr(1)}</td>
                    <td>{(sentence[props.analysisTypeKey]["mood"].mixedScores["joy"] * 100).toFixed(2)}%</td>
                    <td>{(sentence[props.analysisTypeKey]["mood"].mixedScores["anger"] * 100).toFixed(2)}%</td>
                    <td>{(sentence[props.analysisTypeKey]["mood"].mixedScores["fear"] * 100).toFixed(2)}%</td>
                    <td>{(sentence[props.analysisTypeKey]["mood"].mixedScores["sadness"] * 100).toFixed(2)}%</td>
                </tr>
            );
        });
    }

    return (
        <table className="table table-hover">
            <thead>
                <tr>
                    <th className="col" style={{width: columnWidth}}>Sentence</th>
                    <th className="col" style={{width: columnWidth}}>Strongest Mood</th>
                    <th className="col" style={{width: columnWidth}}>Joy</th>
                    <th className="col" style={{width: columnWidth}}>Anger</th>
                    <th className="col" style={{width: columnWidth}}>Fear</th>
                    <th className="col" style={{width: columnWidth}}>Sadness</th>
                </tr>
            </thead>
            <tbody>
                {fillTable()}
            </tbody>
        </table>
    );

}

export default MoodSentenceTable;
