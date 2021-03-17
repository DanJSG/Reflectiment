import React, { useEffect, useRef, useState } from 'react';
import {getAverageScores, getMaxScores} from './services/resultprocessingservice';
import {generateMoodCsv, generateReflectionCsv, generateSentimentCsv} from './services/csvbuilderservice';
import {pickTaggingFunction} from './services/taggingservice';
import ResultsRadios from './ResultsRadios';
import ResultsTable from './ResultsTable';
import ResultsTabs from './ResultsTabs';
import SentimentSentenceTable from './tables/SentimentSentenceTable';

function ResultsCard(props) {

    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [analysisFeatures] = useState(["sentiment", "mood", "reflection"])

    const [averageScores, setAverageScores] = useState(null);
    const [maxScores, setMaxScores] = useState(null);
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
                        <ResultsRadios selectAnalysisFeature={selectAnalysisFeature} activeRadioButton={activeRadioButton} />
                        {!props.analysis ? null :
                            <div className="text-justify pt-2" style={{cursor: "default"}}>
                                <p>{taggedSentences}</p>
                                <button onClick={downloadCSV} className="btn btn-primary"><i className="fa fa-download" /> Download as CSV</button>
                                <a style={{display: "none"}} href="/" ref={hiddenDownloadLink}>Hidden File Download</a>
                            </div>
                        }
                    </div>
                </div>
                <hr />
                <div className="row">
                    <div className="col-6 border-right">
                        <h3 className="font-weight-normal text-center card-title">Average Scores</h3>
                        {!averageScores ? null : <ResultsTable scores={averageScores} />}
                    </div>
                    <div className="col-6">
                        <h3 className="font-weight-normal text-center card-title">Sentence Scores</h3>
                        {/* {!maxScores ? null : <ResultsTable scores={maxScores} />} */}
                        {maxScores && activeRadioButton === 0 ? <SentimentSentenceTable analysisTypeKey={analysisTypeKeys[activeTab]} sentences={props.analysis.sentences}/> : null}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;
