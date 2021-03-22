import React from 'react';
import MainContent from '../components/layout/MainContent';

/**
 * Component for the about page, containing details about the application.
 * 
 * @returns DOM elements for the about page
 */
function AboutPage() {
    return (
        <div className='w-100 h-100'>
            <MainContent>
                <h1 className="p-4 display-4 font-weight-normal">About This Application</h1>
                <p className="pl-3 pr-3 text-justify">
                    This application aims to automatically analyse a section of text for sentiment, mood and reflection.
                </p>
                <p>
                    More to come.
                </p>
            </MainContent>
        </div>
    );
}

export default AboutPage;
