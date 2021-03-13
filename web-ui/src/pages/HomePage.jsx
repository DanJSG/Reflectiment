import React, { useRef, useState } from 'react';
import NavBar from '../components/NavBar';
import MainContent from '../components/MainContent';
import TextSubmissionCard from '../components/TextSubmissionCard';
import ResultsCard from '../components/ResultsCard';
import downArrow from '../resources/down.svg'

function HomePage() {

    const [analysis, setAnalysis] = useState(null);
    const resultsRef = useRef(null);

    const handleAnalysisResponse = async (analysis) => {
        setAnalysis(analysis);
        scrollToResults();
    }

    const scrollToResults = () => {
        resultsRef.current.scrollIntoView({behaviour: "smooth"});
    }

    return (
        <div className='w-100 h-100'>
            <NavBar />
            <MainContent>
                {/* Heading and summary */}
                <h1 className="p-4 display-4 font-weight-normal">Automated Text Analyzer</h1>
                <p className="pl-3 pr-3 text-justify">
                    This tool automatically analyzes sentiment, mood, and reflection in a piece of written text. 
                    To get started, simply type some text into the box below and click "Analyze". 
                    The results will then be shown below automatically.
                </p>
                <TextSubmissionCard handleAnalysisResponse={handleAnalysisResponse} />
                <div className="p-4"></div>
                {
                    analysis === null ? null : 
                    <React.Fragment>
                        {/* <img className="p-5" src={downArrow} alt="" width="25%"/>
                        <h1 className="pb-3 font-weight-normal">Results</h1> */}
                        <ResultsCard analysis={analysis}/>
                    </React.Fragment>
                }
            </MainContent>
            <div ref={resultsRef}></div>
        </div>
    )

}

export default HomePage;
