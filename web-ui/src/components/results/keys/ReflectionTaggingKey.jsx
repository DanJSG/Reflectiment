import React from 'react';

/**
 * Colour key component for the reflection text tagger.
 * 
 * @returns the DOM elements to render
 */
function ReflectionTaggingKey() {
    return (
        <div className="p-1 px-2 ml-auto bg-light rounded border">
            <div style={{transform: "translateY(0rem)", height: "0px"}} className="text-center">Reflection Intensity</div>
            <div style={{width: "20rem", height: "1.65rem", backgroundImage: "linear-gradient(to right, rgba(128, 128, 0, 0.1), rgba(33, 223, 0, 1))", borderRadius: "0.25rem"}}></div>
            <div style={{height: "0px"}} className="d-flex p-0">
                <span style={{transform: "translateY(-1.65rem)"}} className="pl-1">0%</span>
                <span style={{transform: "translateY(-1.65rem)"}} className="pr-1 ml-auto">100%</span>
            </div>
        </div>
    )
}

export default ReflectionTaggingKey;
