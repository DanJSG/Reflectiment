import React, { useEffect, useState } from 'react';
import {getAverageScores, getMaxScores} from '../services/resultprocessing';
import ResultsTable from './ResultsTable';

function ResultsCard(props) {

    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [analysisFeatures] = useState(["sentiment", "mood", "reflection"])

    const [averageScores, setAverageScores] = useState(null);
    const [maxScores, setMaxScores] = useState(null);
    const [activeTab, setActiveTab] = useState(0);
    const [taggedSentences, setTaggedSentences] = useState(null);
    const [activeRadioButton, setActiveRadioButton] = useState(0);

    const switchTab = (e) => {
        e.preventDefault();
        const tabId = e.nativeEvent.submitter.id;
        e.target.elements[activeTab].classList.toggle("active");
        e.target.elements[tabId].classList.toggle("active");
        setActiveTab(tabId)
        showResults(analysisTypeKeys[tabId], tabId);
    }

    const showResults = (analysisTypeKey, tabId) => {
        const averages = getAverageScores(props.analysis.sentences, analysisTypeKey);
        const maxes = getMaxScores(props.analysis.sentences, analysisTypeKey);
        setAverageScores(averages);
        setMaxScores(maxes);
        setActiveRadioButton(activeRadioButton);
        tagText(analysisFeatures[activeRadioButton], tabId);
    }

    const selectAnalysisFeature = (e) => {
        const analysisFeature = e.target.id.replace("Radio", '');
        if(analysisFeature === "sentiment") {
            setActiveRadioButton(0);
        } else if(analysisFeature === "mood") {
            setActiveRadioButton(1);
        } else {
            setActiveRadioButton(2);
        }
        tagText(e.target.id.replace("Radio", ''), activeTab);
    }

    // TODO refactor into new file
    const tagSentiment = (sentence, scores, index) => {
        const colorVal = scores.score;
        const colorStyle = {
            backgroundColor: colorVal > 0 ? `rgba(0, 255, 0, ${Math.abs(colorVal)})` : `rgb(255, 0, 0, ${Math.abs(colorVal)})`
        }
        return <span key={index} style={colorStyle}>{sentence}&nbsp;</span>;
    }

    // TODO refactor into new file
    const tagMood = (sentence, scores, index) => {
        console.log(scores.mixedScores);
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
        return (
            <span key={index} style={colorStyle}>{sentence}&nbsp;</span>
        )

        // console.log(sentence);
    }

    // TODO refactor into new file
    // TODO review using a temperature style colouring scheme
    const tagReflection = (sentence, scores, index) => {
        const colorVal = scores.score;
        const colorStyle = {
            backgroundColor: `rgba(${128 - (colorVal * 95)}, ${128 + (colorVal * 95)}, 0, ${colorVal})`
        }
        return <span key={index} style={colorStyle}>{sentence}&nbsp;</span>;
    }
    
    const pickTaggingFunction = (analysisFeature) => {
        if(analysisFeature === "sentiment") {
            return tagSentiment;
        } else if(analysisFeature === "mood") {
            return tagMood;
        } else {
            return tagReflection;
        }
    }

    const tagText = (analysisFeature, tabId) => {
        const sentences = props.analysis.sentences;
        const analysisTypeKey = analysisTypeKeys[tabId];
        const htmlElements = [];
        const taggingFunction = pickTaggingFunction(analysisFeature);
        for(let i = 0; i < sentences.length; i++) {
            const sentence = sentences[i];
            htmlElements.push(taggingFunction(sentence.sentence, sentence[analysisTypeKey][analysisFeature], i));
        }
        setTaggedSentences(htmlElements);
    }

    useEffect(() => {
        if(!props.analysis) {
            return;
        }
        showResults(analysisTypeKeys[activeTab], activeTab);
    }, [props.analysis, analysisTypeKeys])

    return (
        <div className="card w-100 shadow-sm border-1 row">
            <div className="card-header">
                <h1 className="font-weight-normal">Results</h1>
                    <form onSubmit={switchTab}>
                        <ul className="nav nav-tabs card-header-tabs">
                            <li className="nav-item">
                                <button className="btn-link nav-link active" id={0}>Lexical</button>
                            </li>
                            <li className="nav-item">
                                <button className="btn-link nav-link" id={1}>Machine Learning</button>
                            </li>
                            <li className="nav-item">
                                <button className="btn-link nav-link" id={2}>Combined</button>
                            </li>
                        </ul>
                    </form>
            </div>
            <div className="container-fluid p-3">
                <div className="row p-3">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Tagged Text</h2>
                        <form className="d-flex pb-3">
                            <div className="form-check mr-3">
                                <input onChange={selectAnalysisFeature} type="radio" name="analysisFeatureRadios" id="sentimentRadio" className="form-check-input mt-2" checked={activeRadioButton === 0}/>
                                <label htmlFor="sentimentRadio" className="form-check-label" style={{fontSize: "1rem"}}>Sentiment</label>
                            </div>
                            <div className="form-check mr-3">
                                <input onChange={selectAnalysisFeature} type="radio" name="analysisFeatureRadios" id="moodRadio" className="form-check-input mt-2" checked={activeRadioButton === 1}/>
                                <label htmlFor="moodRadio" className="form-check-label" style={{fontSize: "1rem"}}>Mood</label>
                            </div>
                            <div className="form-check mr-3">
                                <input onChange={selectAnalysisFeature} type="radio" name="analysisFeatureRadios" id="reflectionRadio" className="form-check-input mt-2" checked={activeRadioButton === 2}/>
                                <label htmlFor="reflectionRadio" className="form-check-label" style={{fontSize: "1rem"}}>Reflection</label>
                            </div>
                        </form>
                        {!props.analysis ? null :
                            <div>
                                {/* <p>{props.analysis.text}</p> */}
                                <p>{taggedSentences}</p>
                            </div>
                        }
                    </div>
                </div>
                <hr/>
                <div className="row">
                    <div className="col-6 border-right">
                        <h3 className="font-weight-normal text-center card-title">Average Scores</h3>
                        {!averageScores ? null : <ResultsTable scores={averageScores} />}
                    </div>
                    <div className="col-6">
                        <h3 className="font-weight-normal text-center card-title">Maximum Scores</h3>
                        {!maxScores ? null : <ResultsTable scores={maxScores} />}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;
