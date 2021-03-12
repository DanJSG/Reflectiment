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
            return null;
        }
        return response.json();
    }).then(json => {
        if(!json) {
            return null;
        }
        return json
    }).catch(error => {
        console.error(error);
    })
}
