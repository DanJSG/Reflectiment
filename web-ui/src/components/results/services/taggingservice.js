import React from 'react';
import Tippy from '@tippyjs/react'
import {followCursor} from 'tippy.js';
import 'tippy.js/dist/tippy.css';
import ReflectionTooltipContent from '../tooltips/ReflectionTooltipContent';

const tagSentiment = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: colorVal > 0 ? `rgba(0, 255, 0, ${Math.abs(colorVal)})` : `rgb(255, 0, 0, ${Math.abs(colorVal)})`
    }
    const tooltipContent = (
        <div>
            <li style={{listStyleType: 'none'}}><b>Label:</b> {scores.label.charAt(0).toUpperCase() + scores.label.substr(1)}</li>
            <li style={{listStyleType: 'none'}}><b>Intensity:</b> {(scores.score * 100).toFixed(2)}%</li>
        </div>
    );
    return (
        <Tippy placement="top" key={index} content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    );
}

const tagMood = (sentence, scores, index) => {
    let colorStyle = {
        backgroundColor: `rgba(0, 0, 0, 0)`,
        color: "#212529"
    };
    if(scores.label === "anger") {
        colorStyle.backgroundColor = `rgba(255, 0, 0, ${scores.score})`;
    } else if(scores.label === "sadness") {
        colorStyle.backgroundColor = `rgba(0, 0, 255, ${scores.score})`;
        colorStyle.color = scores.score > 0.5 ? "#f8f9fa" : colorStyle.color;
    } else if(scores.label === "fear") {
        colorStyle.backgroundColor = `rgba(128, 0, 128, ${scores.score})`;
        colorStyle.color = scores.score > 0.5 ? "#f8f9fa" : colorStyle.color;
    } else {
        colorStyle.backgroundColor = `rgba(255, 255, 0, ${scores.score})`;
    }
    const tooltipContent = (
        <div>
            {Object.keys(scores.mixedScores).map((key, index) => <li key={index} style={{listStyleType: "none"}}><b>{key.charAt(0).toUpperCase() + key.substr(1)}:</b> {(scores.mixedScores[key] * 100).toFixed(2)}%</li>)}
        </div>
    )
    return (
        <Tippy key={index} placement="top" content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    )
}

const tagReflection = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: `rgba(${128 - (colorVal * 95)}, ${128 + (colorVal * 95)}, 0, ${colorVal})`
    }
    console.log(scores);
    const tooltipContent = <ReflectionTooltipContent scores={scores} />;
    return (
        <Tippy key={index} placement="top" content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    );
}

export const pickTaggingFunction = (analysisFeature) => {
    if(analysisFeature === "sentiment") {
        return tagSentiment;
    } else if(analysisFeature === "mood") {
        return tagMood;
    } else {
        return tagReflection;
    }
}
