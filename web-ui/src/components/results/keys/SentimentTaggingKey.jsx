import React from 'react';

function SentimentTaggingKey() {
    return (
        <div className="p-1 ml-auto bg-light rounded border">
            <span className="px-3"><i style={{color: "rgba(0, 255, 0, 1)"}} className="fa fa-square"></i>&nbsp;Positive</span>
            <span className="pr-3"><i style={{color: "rgba(200, 200, 200, 0.75)"}} className="fa fa-square"></i>&nbsp;Neutral</span>
            <span className="pr-3"><i style={{color: "rgba(255, 0, 0, 1)"}} className="fa fa-square"></i>&nbsp;Negative</span>
        </div>
    )
}

export default SentimentTaggingKey;
