/**
 * Get the background colour styling of a table row depending on whether it is a 
 * maximum, minimum or standard row.
 * 
 * @param {int} index the current row index in the table
 * @param {int} maxIndex the index of the row with the maximum value
 * @param {int} minIndex the index of the row with the minimum value
 * @returns the row colour CSS styling
 */
export const getRowStyle = (index, maxIndex, minIndex) => {
    if(index === maxIndex) {
        return {backgroundColor: "rgba(0, 255, 0, 0.25)"};
    } else if(index === minIndex) {
        return {backgroundColor: "rgba(255, 0, 0, 0.25)"};
    } else {
        return null;
    }
}
