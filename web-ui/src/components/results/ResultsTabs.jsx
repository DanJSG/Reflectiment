import React from 'react';

/**
 * The 3 tab picker for the results card component.
 * 
 * @param {Object} props component properties passed from parent
 * @returns the DOM elements to render
 */
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
