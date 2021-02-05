package com.dtj503.offlinedevelopment.utils;

import java.util.List;

public class Averages {

    public static float mean(List<Float> list) {
        float sum = 0;
        for(Float num : list) {
            sum += num;
        }
        return sum / list.size();
    }

}
