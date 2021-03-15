import React from 'react';

function ResultsTabs(props) {

    return (
        <form onSubmit={props.switchTab}>
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
    );

}

export default ResultsTabs;
