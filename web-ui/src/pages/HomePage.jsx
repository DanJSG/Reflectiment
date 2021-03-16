import React, { useRef, useState } from 'react';
import NavBar from '../components/layout/NavBar';
import MainContent from '../components/layout/MainContent';
import TextSubmissionCard from '../components/submission/TextSubmissionCard';
import ResultsCard from '../components/results/ResultsCard';
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
            <MainContent>
                <h1 className="p-4 display-4 font-weight-normal">Automated Text Analyzer</h1>
                <p className="pl-3 pr-3 text-justify">
                    This tool automatically analyzes sentiment, mood, and reflection in a piece of written text. 
                    To get started, simply type some text into the box below and click "Analyze". 
                    The results will then be shown below automatically.
                </p>
                <TextSubmissionCard handleAnalysisResponse={handleAnalysisResponse} />
                {
                    analysis === null ? null : 
                    <React.Fragment>
                        <img className="p-5" src={downArrow} alt="" width="25%"/>
                        <ResultsCard analysis={analysis}/>
                    </React.Fragment>
                }
                <div className="p-4"></div>
                <div ref={resultsRef}></div>
            </MainContent>
        </div>
    )

}

export default HomePage;
