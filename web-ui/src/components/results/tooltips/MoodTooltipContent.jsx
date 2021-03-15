import React from 'react';

function MoodTooltipContent(props) {
    return (
        <div>
            {Object.keys(props.scores.mixedScores).map((key, index) => {
                return <li key={index} style={{listStyleType: "none"}}><b>{key.charAt(0).toUpperCase() + key.substr(1)}:</b> {(props.scores.mixedScores[key] * 100).toFixed(2)}%</li> 
            })}
        </div>
    );
}

export default MoodTooltipContent;
