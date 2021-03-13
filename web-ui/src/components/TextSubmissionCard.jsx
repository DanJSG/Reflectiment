import React from 'react';
import {sendAnalysisRequest} from '../services/analysisservice';

function TextSubmissionCard(props) {

    const analyseText = async (e) => {
        e.preventDefault();
        let text = e.target.elements.textSubmissionBox.value;
        if(text === null || text === undefined) {
            throw Error("Failed to send analysis request.");
        }
        const response = await sendAnalysisRequest(text.trim());
        props.handleAnalysisResponse(response);
    }

    return (
        <div className="card w-100 shadow-sm border-1">
            <div className="card-header">
                <h1 className="font-weight-normal">Text Input</h1>
            </div>
            <form className="p-3" onSubmit={analyseText}>
                <div className="form-group">
                    <textarea className="form-control" placeholder="Enter your text here..." name="textSubmissionBox" id="textSubmissionBox0" rows="12" style={{resize: "none"}} />
                </div>
                <div className="form-group">
                    <button className="btn btn-primary pl-4 pr-4">Analyze</button>
                </div>
            </form>
        </div>
    )
}

export default TextSubmissionCard;
