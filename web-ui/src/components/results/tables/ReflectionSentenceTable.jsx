import React, { useEffect, useState } from 'react';

function ReflectionSentenceTable(props) {

    const [columnWidth, setColumnWidth] = useState("16.67%");
    const [features, setFeatures] = useState(null);

    const [tableHeader, setTableHeader] = useState(null);
    const [tableBody, setTableBody] = useState(null);

    const fillTableHeader = () => {
        const headings = features.map(feature => feature.charAt(0).toUpperCase() + feature.substr(1));
        return (
            <tr>
                <th className="col" style={{width: columnWidth}}>Sentence</th>
                <th className="col" style={{width: columnWidth}}>Overall</th>
                {headings.map((heading, index) => <th className="col" key={index} style={{width: columnWidth}}>{heading}</th>)}
            </tr>
        )
    }

    const fillTable = () => {
        const scores = props.sentences.map(sentence => sentence[props.analysisTypeKey]["reflection"].categoryScores);
        const rows = [];
        for(let i = 0; i < scores.length; i++) {
            const row = [];
            features.forEach(feature => row.push(scores[i][feature]));
            rows.push(row.map(score => <td>{(score * 100).toFixed(2)}%</td>))
        }
        return rows.map((row, i) => (
            <tr>
                <td>{props.sentences[i].sentence}</td>
                <td>{(props.sentences[i][props.analysisTypeKey]["reflection"].score * 100).toFixed(2)}%</td>
                {row}
            </tr>
        ));
    }

    useEffect(() => {
        if(!features) {
            return;
        }
        setColumnWidth(String(100 / (features.length + 2)) + "%");
        setTableHeader(fillTableHeader());
        setTableBody(fillTable());
    }, [features])

    useEffect(() => {
        if(!props.sentences[0][props.analysisTypeKey]["reflection"].categoryScores) {
            setFeatures([]);
        } else {
            setFeatures(Object.keys(props.sentences[0][props.analysisTypeKey]["reflection"].categoryScores));
        }
        // console.log(props.sentences[0][props.analysisTypeKey]["reflection"].categoryScores);
    }, [props.sentences, props.analysisTypeKey])

    return (
        <table className="table table-hover">
            <thead>
                {tableHeader}
            </thead>
            <tbody>
                {tableBody}
            </tbody>
        </table>
    )
}

export default ReflectionSentenceTable;
