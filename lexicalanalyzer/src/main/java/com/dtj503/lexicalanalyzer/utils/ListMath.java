package com.dtj503.lexicalanalyzer.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing some mathematical functions for lists.
 *
 * @author Dan Jackson (dtj503@york.ac.uk)
 */
public class ListMath {

    /**
     * Method to calculate the mean of a list of floats.
     *
     * @param list the list of <code>float</code> values
     * @return the mean of the list
     */
    public static float mean(List<Float> list) {
        float sum = 0;
        for(Float num : list) {
            sum += num;
        }
        return sum / list.size();
    }

    /**
     * Method to calculate the hadamard product (piecewise multiplication) of two lists. This takes two lists (vectors)
     * and multiplies each corresponding element with each other, producing another list (vector) with the same number
     * of elements.
     *
     * @param a the first list
     * @param b the second list
     * @return another list with the same dimensions of <code>a</code> and <code>b</code>
     */
    public static List<Float> hadamardProduct(List<Float> a, List<Float> b) {
        if(a.size() != b.size()) {
            return null;
        }
        List<Float> productVector = new ArrayList<>(a.size());
        for(int i = 0; i < a.size(); i++) {
            productVector.add(a.get(i) * b.get(i));
        }
        return productVector;
    }

}
