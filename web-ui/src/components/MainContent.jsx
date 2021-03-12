import React from 'react';

function MainContent(props) {
    return (
        <div className="container-fluid">
            <div className="d-flex flex-column align-items-center">
                <div className="w-50 d-flex flex-column align-items-center">
                    {props.children}
                </div>
            </div>
        </div>

    )
}

export default MainContent;