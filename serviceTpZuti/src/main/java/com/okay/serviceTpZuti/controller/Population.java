package com.okay.serviceTpZuti.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Map;
import org.apache.hadoop.hbase.client.Table;

public class Population {
    private Individual population[];
    private  double populationFitness = -1;



    private static String row = null;
    private  static  String master = null;

    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }
    //chromosomeLength---染色体长度--------题目数量pub

//    public Population (int populationSize, int chromosomeLength) {
//        this.population = new Individual[populationSize];
//
//        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
//            Individual individual = new Individual(chromosomeLength);
//            this.population[individualCount] = individual;
//        }
//    }

    public static String getMaster() {
        return master;
    }

    public static void setMaster(String master) {
        Population.master = master;
    }

    public Population (int populationSize, Map<String, String> typeCount, int topic_id, Table table, Demiurge demiurge, String master) {
        this.population = new Individual[populationSize];
        this.master = master;
        System.out.println(topic_id + " new Individual");
        int geneIndex = 0;
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            Individual individual = new Individual(typeCount, topic_id, table, demiurge, individualCount);
            //geneIndex += individual.getGene_count();
            this.population[individualCount] = individual;
        }
    }

    public Individual[] getIndividuals() {
        return this.population;
    }

    public Individual getFittest(int offset) {
        Arrays.sort(this.population, new Comparator<Individual>() {
            //@Override
            public int compare(Individual o1, Individual o2){
                if (o1.getFitness() > o2.getFitness()) {
                    return  -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }

        });
        return this.population[offset];
    }

    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public int size() {
        return this.population.length;
    }

    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return population[offset];
    }

    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length -1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }
}
