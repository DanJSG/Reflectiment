import React from 'react';

function TextSubmissionCard() {
    return (
        <div className="card w-100 p-3 shadow-sm border-1">
            <form>
                <div class="form-group">
                    <label for="textSubmissionBox" className="h3 card-title font-weight-normal">Input Your Text:</label>
                    <textarea class="form-control" placeholder="Enter your text here..." name="textSubmissionBox" id="textSubmissionBox0" rows="12" style={{resize: "none"}} />
                </div>
                <div className="form-group">
                    <button className="btn btn-primary pl-4 pr-4">Analyze</button>
                </div>
            </form>
        </div>
    )
}

export default TextSubmissionCard;
