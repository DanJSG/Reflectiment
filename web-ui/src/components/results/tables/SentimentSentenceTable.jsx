import React from 'react';

function SentimentSentenceTable(props) {

    const fillTable = () => {
        return props.sentences.map(sentence => {
            return (
                <tr>
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
                    <th scope="col" style={{width: "33%"}}>Sentence</th>
                    <th className="col" style={{width: "33%"}}>Sentiment Intensity</th>
                    <th className="col" style={{width: "33%"}}>Label</th>
                </tr>
            </thead>
            <tbody>
                {fillTable()}
            </tbody>
        </table>
    );

}

export default SentimentSentenceTable;
