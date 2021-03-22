import React from 'react';

/**
 * Main content layout component, which takes a set of children and wraps them within layout divs.
 * 
 * @param {Object} props the component properties passed down from the parent
 * @returns the DOM elements to render, including the children passed through
 */
function MainContent(props) {
    return (
        <div className="container-fluid">
            <div className="d-flex flex-column align-items-center">
                <div style={{width: "65%"}} className="d-flex flex-column align-items-center">
                    {props.children}
                </div>
            </div>
        </div>

    )
}

export default MainContent;
