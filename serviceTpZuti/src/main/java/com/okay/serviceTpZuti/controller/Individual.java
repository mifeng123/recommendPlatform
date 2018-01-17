package com.okay.serviceTpZuti.controller;

import java.util.Map;
//import org.apache.hadoop.hbase.HBaseConfiguration;
import com.okay.serviceTpZuti.domain.Gene;
import com.okay.serviceTpZuti.domain.Leg;
import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.KeyValue;
//import org.apache.hadoop.hbase.client.HTable;
//import org.apache.hadoop.hbase.client.HTablePool;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.client.Get;
//import org.apache.hadoop.hbase.filter.*;
//import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
//import org.apache.hadoop.hbase.util.Bytes;
//import java.util.ArrayList;
//import java.util.List;

import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.client.Connection;
//import org.apache.hadoop.hbase.client.ConnectionFactory;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.Cell;

public class Individual {
    private static Configuration hbaseConfig=null;
    //将gene换成单个题，题有自身排序分
    //染色体----题集
    private int[] chromosome;
    private Leg[] Qchromosome;
    private double fitness;

//    public int getGene_count() {
//        return gene_count;
//    }
//
//    public void setGene_count(int gene_count) {
//        this.gene_count = gene_count;
//    }
//
//    private  int gene_count;

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }
    //gene 0/1 替换成题号
    public Individual(int QchromosomeLength, Map<String, String> typeCount, int topic_id){
        int legIndex = 0;
        this.Qchromosome = new Leg[QchromosomeLength];
        for (String type : typeCount.keySet()) {
            String count = typeCount.get(type);
            Leg leg = new Leg(type, topic_id, Integer.parseInt(count));
            //this.chromosome = new int[chromosomeLength];
            for (int i = 0; i < leg.getLegLength(); i++) {
                leg.getLeg()[i] = new Gene();
            }
            this.setQLeg(legIndex, leg);
            legIndex++;
        }
    }

    public Individual(Map<String, String> typeCount, int topic_id, Table table, Demiurge demiurge, int individualCount){
        //this.setGene_count(geneCount);
        int legIndex = 0;
        this.Qchromosome = new Leg[typeCount.size()];
        for (String type : typeCount.keySet()) {
            String count = typeCount.get(type);
            //System.out.println("Leg leg = fromDemiurge " + type + " " + count);
            Leg leg = fromDemiurge(type, count, topic_id, table, demiurge, individualCount);

            //this.chromosome = new int[chromosomeLength];
            //System.out.println("Individual : legIndex : " + legIndex);
            //System.out.println("Individual : leg : " + leg.getTopic_id());
            this.setQLeg(legIndex, leg);
            //this.setGene_count(leg.getGene_count());
            legIndex++;
        }
    }

    public  int[] getChromosome(){
        return this.chromosome;
    }
    public int getchromosomeLength() {
        return this.chromosome.length;
    }
    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }
    public int getGene(int offset){
        return this.chromosome[offset];
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    public double getFitness() {
        return this.fitness;
    }

    //题集
    public  Leg[] getQChromosome(){
        return this.Qchromosome;
    }
    public int getQchromosomeLength() {
        return this.Qchromosome.length;
    }
    public void setQLeg(int offset, Leg leg) {
        //System.out.println("set1" + leg.getTopic_id());
        //System.out.println("legIndex : " + offset);
        this.Qchromosome[offset] = leg;
        //System.out.println("set2");
    }
    public Leg getQLeg(int offset){
        return this.Qchromosome[offset];
    }
    //    public void setQuestionGene(int offset, Leg leg) {
//        this.Qchromosome[offset] = leg;
//    }
//    public Leg getQuestionGene(int offset){
//        return this.Qchromosome[offset];
//    }
    //输出基因字符串
    public String toStirng(){
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene];
        }
        return  output;
    }

    //从hbase选取题目
    public Leg fromDemiurge(String type, String count, int topic_id, Table table, Demiurge demiurge, int individualCount) {
        int cnt = Integer.parseInt(count);
        Leg ileg = new Leg(type, topic_id, cnt);
        int geneIndex = individualCount * cnt;
        //System.out.println("individualCount * cnt " + geneIndex);
        for (Leg leg : demiurge.getQChromosome()) {
            if (leg.getTopic_id() == topic_id && leg.getType() == type){
                //int id = 0;
                for (int i = 0; i < cnt; i++) {
                    if (leg.getGene(geneIndex) != null) {
                        ileg.setGene(i, leg.getGene(geneIndex));
//                        System.out.println("ileg " + ileg.getType() + " " + ileg.getLegLength());
//                        System.out.println(leg.getGene(geneIndex).getQuestion_id());
//                        System.out.println(leg.getGene(geneIndex).getQuestion_type_id());
                        geneIndex++;
                    }
                }
                //geneIndex ++;
                //System.out.println("geneIndex " + geneIndex);
            }
        }
        ileg.setGene_count(geneIndex);
        return ileg;
    }


    public Gene getOne(Leg leg) {
        //TODO
        System.out.println("getOne");

        int point = (int) Math.floor(Math.random()*leg.getMax());
        Gene gene = leg.getGene(point);



        return gene;
    }
}
