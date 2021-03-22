import React, { useEffect, useState } from 'react';
import {getRowStyle} from './services/rowstyler';

/**
 * The reflection results table component, a table which lists sentences along with each of their 
 * associated reflection component scores and the overall intensity percentage.
 * 
 * @param {Object} props component properties passed down from parent
 * @returns the DOM elements to render
 */
function ReflectionSentenceTable(props) {

    const [columnWidth, setColumnWidth] = useState("16.67%");
    const [features, setFeatures] = useState(null);
    const [analysisFeature, setAnalysisFeature] = useState("reflection");
    const [tableHeader, setTableHeader] = useState(null);
    const [tableBody, setTableBody] = useState(null);

    /**
     * Fills the table header with the component feature labels.
     * 
     * @returns the table's heading row
     */
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

    /**
     * Fills the table with rows of analysis data.
     * 
     * @returns table rows containing sentences, and its associated analysis information
     */
    const fillTable = () => {
        const scores = props.sentences.map(sentence => sentence[props.analysisTypeKey][analysisFeature].categoryScores);
        const rows = [];
        for(let i = 0; i < scores.length; i++) {
            const row = [];
            features.forEach(feature => row.push(scores[i][feature]));
            rows.push(row.map((score, index) => <td key={index}>{(score * 100).toFixed(2)}%</td>))
        }
        return rows.map((row, i) => (
            <tr key={i} style={getRowStyle(i, props.maxIndex, props.minIndex)}>
                <td>{props.sentences[i].sentence}</td>
                <td>{(props.sentences[i][props.analysisTypeKey][analysisFeature].score * 100).toFixed(2)}%</td>
                {row}
            </tr>
        ));
    }

    /**
     * Called whenever the table features are updated (the individual component scores). Updates the 
     * table headers and values and refits the column widths to suit the new number of headings.
     */
    useEffect(() => {
        if(!features) {
            return;
        }
        setColumnWidth(String(100 / (features.length + 2)) + "%");
        setTableHeader(fillTableHeader());
        setTableBody(fillTable());
    }, [features])

    /**
     * Called whenever the props update. Sets the table's features and whether or not to use the reflection 
     * scores or the modified reflection scores.
     */
    useEffect(() => {
        if(!props.sentences[0][props.analysisTypeKey]["reflection"].categoryScores) {
            setFeatures([]);
        } else {
            setFeatures(Object.keys(props.sentences[0][props.analysisTypeKey]["reflection"].categoryScores));
        }
        setAnalysisFeature(!props.useReflectionModifiers ? "reflection" : "modifiedReflection");
    }, [props.sentences, props.analysisTypeKey, props.useReflectionModifiers])

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
