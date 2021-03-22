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
                <div className="pl-3 pr-3 text-justify">
                    <p>
                        This is an automated text analysis application for determining sentiment, author mood, and reflectivity in a piece of text.
                        The core analysis system is comprised of two parts: an algorithmic, lexicon-based analyzer; and a machine learning based analyser.
                    </p>
                    <p>
                        The lexicon-based analyzer is written in Java and makes use of the <a href="https://hlt-nlp.fbk.eu/technologies/sentiwords">SentiWords</a> dictionary for sentiment
                        analysis, the <a href="https://saifmohammad.com/WebPages/NRC-Emotion-Lexicon.htm">NRC Word-Emotion Association Lexicon</a> for mood analysis, and a personally devised
                        lexicon built based on the research done in <a href="http://oro.open.ac.uk/48840/">a paper by Thomas Ullman</a>. These lexicons are combined with a set of rules for negation and modification.
                    </p>
                    <p>
                        The machine learning based analyzer is written in Python and makes use of the Keras API for Tensorflow to implement the neural networks.
                        The sentiment analysis used a deep Convolutional-LSTM network trained on the <a href="https://nlp.stanford.edu/sentiment/">Stanford Sentiment Treebank (SST) dataset</a>.
                        The mood analysis used a deep multi-output Bidirectional-LSTM network which was trained using the <a href="http://saifmohammad.com/WebPages/EmotionIntensity-SharedTask.html">dataset
                        from the WASSA-2017 Shared Task on Emotion Intensity</a>. The reflection analysis used another multi-output Bidirectional-LSTM network, and was trained using a dataset manually created for this project.
                    </p>
                </div>
            </MainContent>
        </div>
    );
}

export default AboutPage;
