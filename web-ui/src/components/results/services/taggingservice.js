import React from 'react';
import Tippy from '@tippyjs/react'
import {followCursor} from 'tippy.js';
import 'tippy.js/dist/tippy.css';
import ReflectionTooltipContent from '../tooltips/ReflectionTooltipContent';
import MoodTooltipContent from '../tooltips/MoodTooltipContent';
import SentimentTooltipContent from '../tooltips/SentimentTooltipContent';

/**
 * Tag a sentence with its relevant sentiment scores by styling the sentences background colour.
 * 
 * @param {string} sentence the sentence to tag 
 * @param {Object} scores the sentences scores
 * @param {int} index the index of the sentence
 * @returns a styled text span with a detail tooltip on hover
 */
const tagSentiment = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: colorVal > 0 ? `rgba(0, 255, 0, ${Math.abs(colorVal)})` : `rgb(255, 0, 0, ${Math.abs(colorVal)})`
    }
    const tooltipContent = <SentimentTooltipContent scores={scores}/>;
    return (
        <Tippy placement="top" key={index} interactive={true} content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    );
}

/**
 * Tag a sentence with its relevant mood scores by styling the sentences background colour.
 * 
 * @param {string} sentence the sentence to tag 
 * @param {Object} scores the sentences scores
 * @param {int} index the index of the sentence
 * @returns a styled text span with a detail tooltip on hover
 */
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
    const tooltipContent = <MoodTooltipContent scores={scores}/>;
    return (
        <Tippy key={index} interactive={true} placement="top" content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    )
}

/**
 * Tag a sentence with its relevant reflection scores by styling the sentences background colour.
 * 
 * @param {string} sentence the sentence to tag 
 * @param {Object} scores the sentences scores
 * @param {int} index the index of the sentence
 * @returns a styled text span with a detail tooltip on hover
 */
const tagReflection = (sentence, scores, index) => {
    const colorVal = scores.score;
    const colorStyle = {
        backgroundColor: `rgba(${128 - (colorVal * 95)}, ${128 + (colorVal * 95)}, 0, ${colorVal})`
    }
    const tooltipContent = <ReflectionTooltipContent scores={scores} />;
    return (
        <Tippy key={index} interactive={true} placement="top" content={tooltipContent} followCursor="initial" plugins={[followCursor]}>
            <span className="outline-on-hover" style={colorStyle}>{sentence}&nbsp;</span>
        </Tippy>
    );
}

/**
 * Pick the relevant text tagging function based on the analysis feature.
 * 
 * @param {string} analysisFeature the feature that is being analysed
 * @returns a tagging function
 */
export const pickTaggingFunction = (analysisFeature) => {
    if(analysisFeature === "sentiment") {
        return tagSentiment;
    } else if(analysisFeature === "mood") {
        return tagMood;
    } else {
        return tagReflection;
    }
}
