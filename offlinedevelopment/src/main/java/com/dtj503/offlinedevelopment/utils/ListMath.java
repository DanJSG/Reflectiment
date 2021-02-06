package com.dtj503.offlinedevelopment.utils;

import java.util.ArrayList;
import java.util.List;

public class ListMath {

    public static float mean(List<Float> list) {
        float sum = 0;
        for(Float num : list) {
            sum += num;
        }
        return sum / list.size();
    }

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

    public static List<Float> limit(List<Float> list, float min, float max) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i) > max) {
                list.set(i, 1f);
            } else if(list.get(i) < min) {
                list.set(i, -1f);
            }
        }
        return list;
    }

}
