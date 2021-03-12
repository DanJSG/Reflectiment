import React from 'react';

function ResultsCard() {
    return (
        <div className="card w-100 p-3 shadow-sm border-1 row">
            <div className="container-fluid">
                <div className="row">
                    <div className="col-6 border-right">
                        <h3 className="font-weight-normal text-center card-title">Average Scores</h3>
                    </div>
                    <div className="col-6">
                        <h3 className="font-weight-normal text-center card-title">Maximum Scores</h3>
                    </div>
                </div>
                <hr/>
                <div className="row p-3">
                    <h3 className="font-weight-normal text-center card-title">Tagged Text</h3>
                </div>
            </div>
        </div>
    )
}

export default ResultsCard;