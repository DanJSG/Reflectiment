import React, { useEffect, useState } from 'react';
import {getAverageScores, getMaxScores} from '../services/resultprocessing';
import ResultsTable from './ResultsTable';

function ResultsCard(props) {

    const [averageScores, setAverageScores] = useState(null);
    const [maxScores, setMaxScores] = useState(null);
    const [analysisTypeKeys] = useState(["lexicalScores", "mlScores", "averageScores"]);
    const [activeTab, setActiveTab] = useState(0);

    const switchTab = (e) => {
        e.preventDefault();
        const tabId = e.nativeEvent.submitter.id;
        e.target.elements[activeTab].classList.toggle("active");
        e.target.elements[tabId].classList.toggle("active");
        setActiveTab(tabId)
        showResults(analysisTypeKeys[tabId]);
    }

    const showResults = (analysisTypeKey) => {
        const averages = getAverageScores(props.analysis.sentences, analysisTypeKey);
        const maxes = getMaxScores(props.analysis.sentences, analysisTypeKey);
        setAverageScores(averages);
        setMaxScores(maxes);
    }

    useEffect(() => {
        if(!props.analysis) {
            return;
        }
        showResults(analysisTypeKeys[0]);
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
                <hr/>
                <div className="row p-3">
                    <div className="col-12">
                        <h2 className="font-weight-normal card-title">Tagged Text</h2>
                        {!props.analysis ? null :
                            <div>
                                <p>{props.analysis.text}</p>
                            </div>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;
