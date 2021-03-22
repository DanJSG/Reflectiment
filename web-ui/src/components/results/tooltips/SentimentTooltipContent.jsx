import React from 'react';

/**
 * A tooltip component containing the sentiment label and intensity percentage.
 * 
 * @param {Object} props the object properties passed down from its parents
 * @returns the DOM elements to render
 */
function SentimentTooltipContent(props) {
    return (
        <div>
            <li style={{listStyleType: 'none'}}><b>Label:</b> {props.scores.label.charAt(0).toUpperCase() + props.scores.label.substr(1)}</li>
            <li style={{listStyleType: 'none'}}><b>Intensity:</b> {(props.scores.score * 100).toFixed(2)}%</li>
        </div>
    );
}

export default SentimentTooltipContent;
