/**
 * Submits the given text to the document API endpoint which performs the text
 * analysis. This analysed response is then returned, or an error message is
 * wrapped in an object and returned.
 * 
 * @param {string} text the text to send to the API
 * @returns {Object} the analysed text or a wrapped error message
 */
export const sendAnalysisRequest = async (text) => {
    const url = `http://localhost:8080/api/v1/document`;
    const textSubmission = {
        text: text,
        timestamp: new Date().toISOString().toString()
    }
    return await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(textSubmission)
    }).then(response => {
        if(response.status !== 200) {
            throw Error(parseError(response));
        }
        return response.json();
    }).then(json => {
        return json
    }).catch(error => {
        console.error(error);
        return {error: error.message};
    })
}

/**
 * Parses a HTTP response and returns an appropriate error message based on the 
 * status code.
 * 
 * @param {Object} response the HTTP response
 * @returns {string} the error message
 */
const parseError = (response) => {
    if(response.status === 400) {
        return "There seems to be an issue with the submitted text. Please ensure that there is text to be submitted, and that there are no leading full stops, question marks or exclamation marks.";
    } else {
        return "Sorry, something seems to have gone wrong whilst analyzing your text.";
    }
}
