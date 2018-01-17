package com.okay.serviceTpZuti.domain;

import java.util.ArrayList;

public class Leg {
    private String type;
    private Gene[] leg;
    private int legLength;
    private int topic_id;
    private int gene_count;
    private ArrayList<QuestionGene> qleg;
    private  int wrong;
    private  int max;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }



    public Leg (String type, int topic_id, int legLength) {
        this.setType(type);
        this.setTopic_id(topic_id);
        this.setLegLength(legLength);
        this.setLeg(legLength);
    }

    public Leg (String type, int topic_id, int wrong, String w) {
        this.setType(type);
        this.setTopic_id(topic_id);
        this.setQleg();
        this.setWrong(wrong);
    }

    public int getGene_count() {
        return gene_count;
    }

    public void setGene_count(int gene_count) {
        this.gene_count = gene_count;
    }

    public int getTopic_id() {

        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public Leg (int cnt) {
        this.leg = new Gene[cnt];
    }

    public int getLegLength() {
        return leg.length;
    }

    public void setLegLength(int legLength) {
        this.legLength = legLength;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Gene[] getLeg() {
        return leg;
    }

    public void setLeg(Gene[] leg) {
        this.leg = leg;
    }

    public void setLeg(int legLength) {
        this.leg = new Gene[legLength];
    }

    public Gene getGene(int offset) {
        return leg[offset];

    }

    public void setGene(int offset, Gene gn) {
        this.leg[offset] = gn;
    }

    public ArrayList<QuestionGene> getQleg() {
        return qleg;
    }

    public void setQleg() {
        this.qleg = new ArrayList<QuestionGene>();
    }

}
