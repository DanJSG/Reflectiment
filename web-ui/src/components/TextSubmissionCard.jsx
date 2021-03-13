import React, { useRef } from 'react';
import {sendAnalysisRequest} from '../services/analysisservice';

function TextSubmissionCard(props) {
    
    const loadingButton = useRef(null);
    const analyzeButton = useRef(null);

    const analyseText = async (e) => {
        e.preventDefault();
        loadingButton.current.style.display = "";
        analyzeButton.current.style.display = "none";
        let text = e.target.elements.textSubmissionBox.value;
        if(text === null || text === undefined) {
            throw Error("Failed to send analysis request.");
        }
        const response = await sendAnalysisRequest(text.trim());
        loadingButton.current.style.display = "none";
        analyzeButton.current.style.display = "";
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
                    <button ref={analyzeButton} className="btn btn-primary pl-4 pr-4">Analyze</button>
                    <button ref={loadingButton} disabled className="btn btn-primary pl-4 pr-4 disabled" style={{display: 'none'}}>
                        <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true" />
                        &nbsp;Loading...
                    </button>
                </div>
            </form>
        </div>
    )
}

export default TextSubmissionCard;
