export const getRowStyle = (index, maxIndex, minIndex) => {
    if(index === maxIndex) {
        console.log("returning max");
        return {backgroundColor: "rgba(0, 255, 0, 0.25)"};
    } else if(index === minIndex) {
        return {backgroundColor: "rgba(255, 0, 0, 0.25)"};
    } else {
        return null;
    }
}