import React, { useRef, useState } from 'react';
import {sendAnalysisRequest} from './services/analysisservice';

/**
 * Component for the text submission card, handling the text area for the text input and 
 * the submit button.
 * 
 * @param {Object} props the component properties passed through from the parent
 * @returns the relevant DOM elements and event handlers
 */
function TextSubmissionCard(props) {
    
    const loadingButton = useRef(null);
    const analyzeButton = useRef(null);
    const [errorMessage, setErrorMessage] = useState(null);
    const [isAnalyzeButtonDisabled, setIsAnalyzeButtonDisabled] = useState(true);

    /**
     * Handle the text analysis submission. Switches the displayed buttons to
     * provide the user with visual feedback that the request is loading, and sends the text to
     * the backend API for analysis. The API's response is then passed back to the parent component,
     * or if there is an error message, this is displayed.
     * 
     * @param {Object} e the form submission event
     * @returns undefined
     */
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
        if(response.error) {
            console.log(response.error);
            setErrorMessage(response.error);
            props.handleAnalysisResponse(null);
            return;
        }
        setErrorMessage(null);
        props.handleAnalysisResponse(response);
    }

    /**
     * Check whether or not the text area is empty. If the text area is empty, then
     * disable the "Analyze" button. Otherwise, enable the button.
     * 
     * @param {Object} e the onChange event from the text area
     */
    const textAreaUpdated = (e) => {
        if(e.target.value.trim() === "") {
            setIsAnalyzeButtonDisabled(true);
        } else {
            setIsAnalyzeButtonDisabled(false);
        }
    }

    return (
        <div className="card w-100 shadow-sm border-1">
            <div className="card-header">
                <h1 className="font-weight-normal">Text Input</h1>
            </div>
            <form className="p-3" onSubmit={analyseText}>
                <div className="form-group">
                    <textarea onChange={textAreaUpdated} className="form-control" placeholder="Enter your text here..." name="textSubmissionBox" id="textSubmissionBox0" rows="12" style={{resize: "none"}} />
                </div>
                {errorMessage ? <p className="text-danger font-weight-bold">{errorMessage}</p> : null}
                <div className="form-group">
                    <button ref={analyzeButton} disabled={isAnalyzeButtonDisabled} className="btn btn-primary pl-4 pr-4">Analyze</button>
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
