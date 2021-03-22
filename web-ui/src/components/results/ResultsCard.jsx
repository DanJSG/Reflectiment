import React, { useEffect, useRef, useState } from 'react';
import {getMaxScoreIndexes, getMinScoreIndexes} from './services/resultprocessingservice';
import {generateMoodCsv, generateReflectionCsv, generateSentimentCsv} from './services/csvbuilderservice';
import {pickTaggingFunction} from './services/taggingservice';
import ResultsRadios from './ResultsRadios';
import ResultsTabs from './ResultsTabs';
import SentimentSentenceTable from './tables/SentimentSentenceTable';
import MoodSentenceTable from './tables/MoodSentenceTable';
import ReflectionSentenceTable from './tables/ReflectionSentenceTable';
import SentimentTaggingKey from './keys/SentimentTaggingKey';
import MoodTaggingKey from './keys/MoodTaggingKey';
import ReflectionTaggingKey from './keys/ReflectionTaggingKey';

/**
 * 
 * Component for the results card. This includes the tagged text section, the results table, as well
 * as all of the relevant keys, navigation tabs and button pickers, and the download button.
 * 
 * @param {Object} props the component properties passed down from the parent
 * @returns the DOM elements to render
 */
function ResultsCard(props) {

    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [analysisFeatures] = useState(["sentiment", "mood", "reflection", "modifiedReflection"])

    const [maxScoreIndexes, setMaxScoreIndexes] = useState(null);
    const [minScoreIndexes, setMinScoreIndexes] = useState(null);
    
    const [useReflectionModifiers, setUseReflectionModifiers] = useState(false);
    const [activeTab, setActiveTab] = useState(0);
    const [taggedSentences, setTaggedSentences] = useState(null);
    const [activeRadioButton, setActiveRadioButton] = useState(0);

    const hiddenDownloadLink = useRef(null);

    /**
     * Switch the active tab of the results card.
     * 
     * @param {Object} e the tab click event
     */
    const switchTab = (e) => {
        e.preventDefault();
        const tabId = e.nativeEvent.submitter.id;
        setUseReflectionModifiers(false);
        e.target.elements[activeTab].classList.toggle("active");
        e.target.elements[tabId].classList.toggle("active");
        setActiveTab(tabId)
        showResults(analysisTypeKeys[tabId], tabId);
    }

    /**
     * Loads the results tables and tags the text.
     * 
     * @param {string} analysisTypeKey the analysis type (eg. lexical, mlScores, averageScores)
     * @param {*} tabId the ID of the currently active tab
     */
    const showResults = (analysisTypeKey, tabId) => {
        setMaxScoreIndexes(getMaxScoreIndexes(props.analysis.sentences, analysisTypeKey));
        setMinScoreIndexes(getMinScoreIndexes(props.analysis.sentences, analysisTypeKey));
        setActiveRadioButton(activeRadioButton);
        const analysisFeature = (useReflectionModifiers) && (activeTab === 0) && (activeRadioButton === 2) ? analysisFeatures[3] : analysisFeatures[activeRadioButton];
        tagText(analysisFeature, tabId);
    }

    /**
     * Chooses the analysis feature (eg. sentiment, mood, or reflection) based on the selected radio button.
     * 
     * @param {Object} e the radio button change event
     */
    const selectAnalysisFeature = (e) => {
        let analysisFeature = e.target.id.replace("Radio", '');
        if(analysisFeature === "sentiment") {
            setUseReflectionModifiers(false);
            setActiveRadioButton(0);
        } else if(analysisFeature === "mood") {
            setUseReflectionModifiers(false);
            setActiveRadioButton(1);
        } else {
            setActiveRadioButton(2);
        }
        tagText(analysisFeature, activeTab);
    }

    /**
     * Tag the text for its relevant analysis features using a specific tagging function.
     * 
     * @param {string} analysisFeature the analysis feature to tag the text for
     * @param {int} tabId the integer ID of the tab
     */
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

    /**
     * Parse the current set of results, and generate a CSV file from them. Then automatically start downloading
     * this file.
     * 
     * @param {Object} e the button click even from the download button
     */
    const downloadCSV = (e) => {
        e.preventDefault();
        const analysisTypeKey = analysisTypeKeys[activeTab];
        const analysisFeature = analysisFeatures[activeRadioButton];
        const sentences = props.analysis.sentences.map(details => details.sentence);
        const scores = props.analysis.sentences.map(details => details[analysisTypeKey][analysisFeature]);
        const downloadName = `${analysisFeature}-${new Date().toISOString()}.csv`;
        let csvBlob;
        if(activeRadioButton === 0) {
            csvBlob = generateSentimentCsv(sentences, scores, analysisTypeKey, analysisFeature);
        } else if (activeRadioButton === 1) {
            csvBlob = generateMoodCsv(sentences, scores, analysisTypeKey, analysisFeature);
        } else {
            csvBlob = generateReflectionCsv(sentences, scores, analysisTypeKey, analysisFeature);
        }
        const fileUrl = window.URL.createObjectURL(csvBlob);
        hiddenDownloadLink.current.href = fileUrl;
        hiddenDownloadLink.current.download = downloadName;
        hiddenDownloadLink.current.click();
    }

    /**
     * Automatically called whenever the analysis props changes.
     */
    useEffect(() => {
        if(!props.analysis) {
            return;
        }
        showResults(analysisTypeKeys[activeTab], activeTab);
    }, [props.analysis, analysisTypeKeys])

    /**
     * Toggles whether or not the modified reflection scores are used or not based on the state of
     * the checkbox.
     */
    const toggleReflectionModifiers = () => {
        const toggledState = !useReflectionModifiers;
        setUseReflectionModifiers(toggledState);
        const analysisFeature = toggledState ? analysisFeatures[3] : analysisFeatures[2];
        tagText(analysisFeature, activeTab);
    }

    return (
        <div className="card w-100 shadow-sm border-1 row">
            <div className="card-header">
                <h1 className="font-weight-normal">Results</h1>
                <ResultsTabs switchTab={switchTab} />
            </div>
            <div className="container-fluid p-3">
                <div className="row px-3 py-2">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Tagged Text</h2>
                        <div className="d-flex">
                            <ResultsRadios selectAnalysisFeature={selectAnalysisFeature} activeRadioButton={activeRadioButton} />
                            {activeRadioButton == 2 && activeTab == 0 ? 
                                <div className="form-check mr-3">
                                    <input type="checkbox" onChange={toggleReflectionModifiers} id="reflectionModifierCheckbox" className="form-check-input mt-2" />
                                    <label htmlFor="reflectionModifierCheckbox" className="form-check-label" style={{fontSize: "1rem"}}>Use reflection modifiers</label>
                                </div>
                                : null
                            }
                            {activeRadioButton === 0 ? <SentimentTaggingKey /> : null}
                            {activeRadioButton === 1 ? <MoodTaggingKey /> : null}
                            {activeRadioButton === 2 ? <ReflectionTaggingKey /> : null}
                        </div>
                        {!props.analysis ? null :
                            <div className="text-justify pt-2" style={{cursor: "default"}}>
                                <p>{taggedSentences}</p>
                            </div>
                        }
                    </div>
                </div>
                <hr />
                <div className="row py-1 px-3">
                    <div className="col-12">
                        <div className="d-flex">
                            <h2 className="font-weight-normal card-title">Sentence Scores</h2>
                            <div className="p-1 px-3 ml-auto border rounded align-self-center">
                                <span className="pr-3"><i style={{color: "rgba(0, 255, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Maximum</span>
                                <span><i style={{color: "rgba(255, 0, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Minimum</span>
                            </div>
                        </div>
                        <div className="py-1"></div>
                        {   
                            taggedSentences && maxScoreIndexes && activeRadioButton === 0 &&
                            <SentimentSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                                    maxIndex={maxScoreIndexes.sentiment} 
                                                    minIndex={minScoreIndexes.sentiment}
                                                    sentences={props.analysis.sentences}
                                                    /> 
                        }
                        {
                            taggedSentences && maxScoreIndexes && activeRadioButton === 1 && 
                            <MoodSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                               sentences={props.analysis.sentences}
                                               maxIndex={maxScoreIndexes.mood}
                                               minIndex={minScoreIndexes.mood}
                                               /> 
                        }
                        {
                            taggedSentences && maxScoreIndexes && activeRadioButton === 2 &&
                            <ReflectionSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                                     sentences={props.analysis.sentences}
                                                     maxIndex={maxScoreIndexes.reflection}
                                                     minIndex={minScoreIndexes.reflection}
                                                     useReflectionModifiers={useReflectionModifiers}
                                                     />
                        }
                        {
                            props.analysis &&
                            <div className="p-2">
                                <button onClick={downloadCSV} className="btn btn-primary"><i className="fa fa-download" /> Download as CSV</button>
                                <a style={{display: "none"}} href="/" ref={hiddenDownloadLink}>Hidden File Download</a>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;
