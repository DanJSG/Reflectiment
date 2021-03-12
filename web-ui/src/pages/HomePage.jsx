import React from 'react';
import NavBar from '../components/NavBar';
import MainContent from '../components/MainContent';
import TextSubmissionCard from '../components/TextSubmissionCard';

function HomePage() {

    return (
        <div className='w-100 h-100'>
            <NavBar />
            <MainContent>
                {/* Heading and summary */}
                <h1 className="p-3 display-4 font-weight-normal">Automated Text Analyzer</h1>
                <p className="pl-3 pr-3 text-justify">
                    This tool automatically analyzes sentiment, mood, and reflection in a piece of written text. 
                    To get started, simply type some text into the box below and click "Analyze". 
                    The results will then be shown below automatically.
                </p>
                {/* Submission Form Card */}
                <TextSubmissionCard />
            </MainContent>
        </div>
    )

}

export default HomePage;