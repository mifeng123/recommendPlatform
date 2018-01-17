package com.okay.serviceTpZuti.controller;

import java.util.Comparator;
import java.util.Map;

class MapValueComparator implements Comparator<Map.Entry<Long, Integer>> {

    public int compare(Map.Entry<Long, Integer> me1, Map.Entry<Long, Integer> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}
