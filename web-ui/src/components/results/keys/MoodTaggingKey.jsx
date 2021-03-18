import React from 'react';

function MoodTaggingKey() {
    return (
        <div className="p-1 ml-auto bg-light rounded border">
            <span className="px-3"><i style={{color: "rgba(255, 0, 0, 1)"}} className="fa fa-square"></i>&nbsp;Anger</span>
            <span className="pr-3"><i style={{color: "rgba(0, 0, 255, 1)"}} className="fa fa-square"></i>&nbsp;Sadness</span>
            <span className="pr-3"><i style={{color: "rgba(128, 0, 128, 1)"}} className="fa fa-square"></i>&nbsp;Fear</span>
            <span className="pr-3"><i style={{color:  "rgba(255, 255, 0, 1)"}} className="fa fa-square"></i>&nbsp;Joy</span>
            <span className="pr-3"><i style={{color: "rgba(200, 200, 200, 0.75)"}} className="fa fa-square"></i>&nbsp;None</span>
        </div>
    )
}

export default MoodTaggingKey;
