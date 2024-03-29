import React, { useRef, useState } from 'react';
import MainContent from '../components/layout/MainContent';
import TextSubmissionCard from '../components/submission/TextSubmissionCard';
import ResultsCard from '../components/results/ResultsCard';
import downArrow from '../resources/down.svg'

/**
 * Component for the home page. Loads the heading, summary, and input card.
 * Renders the results card if the input has been sent and a response has been
 * received.
 * 
 * @returns renders the homepage DOM elements/components
 */
function HomePage() {

    const [analysis, setAnalysis] = useState(null);
    const resultsRef = useRef(null);

    /**
     * Sets the component's state to contain the analysis results and then
     * scrolls the results card into view.
     * 
     * @param {Object} analysis the piece of analysed text
     */
    const handleAnalysisResponse = async (analysis) => {
        setAnalysis(analysis);
        scrollToResults();
    }

    /**
     * Scroll to the results card.
     */
    const scrollToResults = () => {
        resultsRef.current.scrollIntoView({behaviour: "smooth"});
    }

    return (
        <div className='w-100 h-100'>
            <MainContent>
                <h1 className="p-4 pt-5 display-4 font-weight-normal">Automated Text Analyzer</h1>
                <p className="pl-3 pr-3 text-justify">
                    This tool analyzes sentiment, mood, and reflection in writing. Type something into the box below and click "Analyze" to get started. 
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
