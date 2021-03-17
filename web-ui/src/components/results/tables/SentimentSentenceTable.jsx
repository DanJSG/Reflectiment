import React, { useState } from 'react';

function SentimentSentenceTable(props) {

    const [columnWidth] = useState("33%")

    // borderColor: "black", borderStyle: "dashed", borderWidth: "2px", 
    const [maxStyle] = useState({backgroundColor: "rgba(255, 0, 0, 0.25)"});
    const [minStyle] = useState({backgroundColor: "rgba(0, 255, 0, 0.25)"});

    const getRowStyle = (index) => {
        if(index === props.maxIndex) {
            console.log("returning max");
            return maxStyle
        } else if(index === props.minIndex) {
            return minStyle;
        } else {
            return null;
        }
    }

    const fillTable = () => {
        return props.sentences.map((sentence, index) => {
            return (
                <tr key={index} style={getRowStyle(index)}>
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
