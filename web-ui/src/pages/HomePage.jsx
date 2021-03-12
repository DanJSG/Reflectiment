import React from 'react';
import NavBar from '../components/NavBar';
import MainContent from '../components/MainContent';
import TextSubmissionCard from '../components/TextSubmissionCard';
import ResultsCard from '../components/ResultsCard';
import downArrow from '../resources/down.svg'

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
                <img className="p-5" src={downArrow} alt="" width="25%"/>
                <h1 className="pb-3 font-weight-normal">Results</h1>
                <ResultsCard />
                {/* <div className="card w-100 p-3 shadow-sm border-1 row">
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
                </div> */}
            </MainContent>
        </div>
    )

}

export default HomePage;