import React, { useState } from 'react';
import {getRowStyle} from './services/rowstyler';

function SentimentSentenceTable(props) {

    const [columnWidth] = useState("33%")

    const fillTable = () => {
        return props.sentences.map((sentence, index) => {
            return (
                <tr key={index} style={getRowStyle(index, props.maxIndex, props.minIndex)}>
                    <td>{sentence.sentence}</td>
                    <td>{(sentence[props.analysisTypeKey]["sentiment"].score * 100).toFixed(2)}%</td>
                    <td>
                        {sentence[props.analysisTypeKey]["sentiment"].label.charAt(0).toUpperCase() + 
                         sentence[props.analysisTypeKey]["sentiment"].label.substr(1)}
                    </td>
                </tr>
            );
        });
    }

    return (
        <table className="table table-hover">
            <thead>
                <tr>
                    <th className="col" style={{width: columnWidth}}>Sentence</th>
                    <th className="col" style={{width: columnWidth}}>Sentiment Intensity</th>
                    <th className="col" style={{width: columnWidth}}>Label</th>
                </tr>
            </thead>
            <tbody>
                {fillTable()}
            </tbody>
        </table>
    );

}

export default SentimentSentenceTable;
