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

function ResultsCard(props) {

    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [analysisFeatures] = useState(["sentiment", "mood", "reflection"])

    const [maxScoreIndexes, setMaxScoreIndexes] = useState(null);
    const [minScoreIndexes, setMinScoreIndexes] = useState(null);
    
    const [activeTab, setActiveTab] = useState(0);
    const [taggedSentences, setTaggedSentences] = useState(null);
    const [activeRadioButton, setActiveRadioButton] = useState(0);

    const hiddenDownloadLink = useRef(null);

    const switchTab = (e) => {
        e.preventDefault();
        const tabId = e.nativeEvent.submitter.id;
        e.target.elements[activeTab].classList.toggle("active");
        e.target.elements[tabId].classList.toggle("active");
        setActiveTab(tabId)
        showResults(analysisTypeKeys[tabId], tabId);
    }

    const showResults = (analysisTypeKey, tabId) => {
        setMaxScoreIndexes(getMaxScoreIndexes(props.analysis.sentences, analysisTypeKey));
        setMinScoreIndexes(getMinScoreIndexes(props.analysis.sentences, analysisTypeKey));
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
                <ResultsTabs switchTab={switchTab} />
            </div>
            <div className="container-fluid p-3">
                <div className="row p-3">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Tagged Text</h2>
                        <div className="d-flex">
                            <ResultsRadios selectAnalysisFeature={selectAnalysisFeature} activeRadioButton={activeRadioButton} />
                            {activeRadioButton === 0 ? <SentimentTaggingKey /> : null}
                            {activeRadioButton === 1 ? <MoodTaggingKey /> : null}
                            {activeRadioButton === 2 ? <ReflectionTaggingKey /> : null}
                            {/* <div className="pr-2 pb-2 ml-auto">
                                <span className="pr-3"><i style={{color: "rgba(0, 255, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Maximum</span>
                                <span><i style={{color: "rgba(255, 0, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Minimum</span>
                            </div> */}
                        </div>
                        {!props.analysis ? null :
                            <div className="text-justify pt-2" style={{cursor: "default"}}>
                                <p>{taggedSentences}</p>
                            </div>
                        }
                    </div>
                </div>
                <hr />
                <div className="row">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Sentence Scores</h2>
                        <div className="pl-2 pr-2 pb-2 w-25">
                            <span className="pr-3"><i style={{color: "rgba(0, 255, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Maximum</span>
                            <span><i style={{color: "rgba(255, 0, 0, 0.5)"}} className="fa fa-square"></i>&nbsp;Minimum</span>
                        </div>
                        {
                            taggedSentences && maxScoreIndexes && activeRadioButton === 0 ? 
                            <SentimentSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                                    maxIndex={maxScoreIndexes.sentiment} 
                                                    minIndex={minScoreIndexes.sentiment}
                                                    sentences={props.analysis.sentences}
                                                    /> 
                            : 
                            null
                        }
                        {
                            taggedSentences && maxScoreIndexes && activeRadioButton === 1 ? 
                            <MoodSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                               sentences={props.analysis.sentences}
                                               maxIndex={maxScoreIndexes.mood}
                                               minIndex={minScoreIndexes.mood}
                                               /> 
                            : 
                            null
                        }
                        {
                            taggedSentences && maxScoreIndexes && activeRadioButton === 2 ? 
                            <ReflectionSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} 
                                                     sentences={props.analysis.sentences}
                                                     maxIndex={maxScoreIndexes.reflection}
                                                     minIndex={minScoreIndexes.reflection}
                                                     />
                            : 
                            null
                        }
                        {
                            !props.analysis ? null :
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
