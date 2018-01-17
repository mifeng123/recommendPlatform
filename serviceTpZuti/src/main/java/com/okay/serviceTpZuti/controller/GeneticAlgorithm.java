package com.okay.serviceTpZuti.controller;

import java.util.HashMap;
import java.util.Map;

import com.okay.serviceTpZuti.domain.Gene;
import com.okay.serviceTpZuti.domain.Leg;
import org.apache.hadoop.hbase.client.Table;


public class GeneticAlgorithm {
    //种群规模
    private int populationSize;
    //变异率
    private double mutationRate;
    //交叉率
    private double crossoverRate;
    //精英数量
    private int elitismCount;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount){
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }
    //初始化种群
//    public Population initPopulation(int chromosomeLength) {
//        Population population = new Population(this.populationSize, chromosomeLength);
//
//        return population;
//    }

    //初始化种群
    public Population initPopulation(Map<String, String> typeCount, int topic_id, Table table, Demiurge demiurge, String master) {
        System.out.println(topic_id + " initPopulation");
        Population population = new Population(this.populationSize, typeCount, topic_id, table, demiurge, master);
        if (this.populationSize > 1) {
            evalPopulation(population, master);
        }
        return population;
    }
    //计算个体排序分
    public double calcFitness(Individual individual, String master) {
        int correctGenes = 0;
        int score = 0;//排序分
        int type = 0;//题型分
        double difficultys = 0;//难度分
        //System.out.println("calcFitness");
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1,0);
        map.put(2,0);
        map.put(3,0);
        for (int legIndex = 0; legIndex < individual.getQchromosomeLength(); legIndex++) {
            //System.out.println("calc 2");
            //System.out.println("individual.getQchromosomeLength() : " + individual.getQchromosomeLength());
            //System.out.println("individual.getQChromosome()[legIndex].getLegLength() " + individual.getQChromosome()[legIndex].getLegLength());
            for (int geneIndex = 0;geneIndex < individual.getQChromosome()[legIndex].getLegLength(); geneIndex++) {
                //System.out.println("individual.getQchromosomeLength() : " + individual.getQChromosome()[legIndex].getLegLength());
                //System.out.println("q " + individual.getQChromosome()[legIndex].getLeg()[3]);
                score += individual.getQChromosome()[legIndex].getGene(geneIndex).getScore();
                int difficulty = individual.getQChromosome()[legIndex].getGene(geneIndex).getDifficulty();
                if (difficulty == 3 | difficulty == 4) {
                    map.put(3, (map.get(3) + 1));
                } else if (difficulty == 1 | difficulty == 2) {
                    map.put(difficulty, (map.get(difficulty) + 1));
                }
                //System.out.println("calc 3");
            }
            //score += individual.getQChromosome()[geneIndex].getScore();

//            if(individual.getGene(geneIndex) == 1) {
//                correctGenes += 1;
//            }
        }
        int hz = map.get(1) + map.get(2) + map.get(3);
        if (master == "N" | master == "A") {
            difficultys = ( 6 - 6*Math.abs(3/5 - map.get(1)/hz) + 4 - 4*Math.abs(2/5 - map.get(2)/hz) - 4.8*(map.get(3)/hz) );
        } else if (master == "B") {
            difficultys = ( 6 - 6*Math.abs(3/5 - map.get(1)/hz) + 4 - 4*Math.abs(2/5 - map.get(2)/hz) + 2 - 2*Math.abs(1/5 - map.get(3)/hz) );
        } else if (master == "C") {
            difficultys = ( 6 - 6*Math.abs(3/5 - map.get(1)/hz) + 4 - 4*Math.abs(2/5 - map.get(2)/hz) + 3 - 3*Math.abs(3/10 - map.get(3)/hz) );
        }
        double fitness = 0.7 * (double) score / individual.getQchromosomeLength() + 0.3 * difficultys;
        individual.setFitness(fitness);
        return  fitness;
    }
    public void evalPopulation(Population population, String master) {
        //System.out.println("evalPopulation");
        double populationFitness = 0;
        for (Individual individual : population.getIndividuals()){
            populationFitness += calcFitness(individual, master);
        }
        population.setPopulationFitness(populationFitness);
    }
    //计算种群适应度------寻找最大排序分个体
    public Individual evalPopulationMax(Population population, int chronosomeLength, String master){
        double fitness = 0;
        Individual id = population.getIndividual(0);
        for(Individual individual : population.getIndividuals())  {
            double fitness2 = calcFitness(individual, master);
            if (fitness2 > fitness) {
                id = individual;
            }

        }
        return id;
    }

    //判断中止-----循环满足次数
//    public boolean isTerminationConditionMet(LoopBoy loopboy) {
//        if (loopboy.getNumber() == 100) {
//            return true;
//        }
//        return false;
//    }

    //轮盘赌选择
    public Individual selectParent(Population population) {
        //Get Individuals
        Individual individuals[] = population.getIndividuals();

        //Spin roulette wheel 转赌轮盘轮
        double populationFitness = population.getPopulationFitness();
        double roulettewhellPoint = Math.random() * populationFitness;

        //find parent
        double spinWheel = 0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= roulettewhellPoint) {
                return individual;
            }
        }
        return individuals[population.size()-1];
    }

    // 交叉

    public Population crossoverPopulation(Population population, Map<String, String> typeCount, int topic_id, Table table, Demiurge demiurge) {
        //Create new Population
        System.out.println(topic_id + " crossoverPopulation");
        Population newPopulation = new Population(population.size());

        //Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            //Apply crossover to this individual? ---判断是否适合交叉
            if (this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
                //Initialze offspring ---初始化后代
                Individual offspring = new Individual(parent1.getQchromosomeLength(), typeCount, topic_id);
                //find second parent
                Individual parent2 = selectParent(population);
                //Loop genome
                //随机选取交叉点,在各题型，点后的基因全部交叉·
                //题集
                //选取各个题型
                for (String type : typeCount.keySet()) {
                    //lenght += Integer.parseInt(typeCount.get(type));
                    //选取随机交叉点
                    int point = (int) Math.floor(Math.random() * Integer.parseInt(typeCount.get(type)));
                    for (int getIndex = 0; getIndex < typeCount.size(); getIndex++) {
                        //循环个体的染色体组的所有染色体判断染色体的题型是否为所选
//                        System.out.println("getIndex" + getIndex);
//                        System.out.println("typeCount.keySet()" + type);
//                        System.out.println("getQChromosome()[getIndex].getType()" + parent1.getQChromosome()[getIndex].getType());
                        if (parent1.getQChromosome()[getIndex].getType() == type) {
                            //TODO
                            for (int geneIndex = 0; geneIndex < parent1.getQChromosome()[getIndex].getLegLength(); geneIndex++) {
                                //交叉基因
                                if (geneIndex <= point) {
                                    offspring.getQChromosome()[getIndex].getLeg()[geneIndex] = parent1.getQChromosome()[getIndex].getGene(geneIndex);
                                } else {
                                    offspring.getQChromosome()[getIndex].getLeg()[geneIndex] = parent2.getQChromosome()[getIndex].getGene(geneIndex);
                                }
                            }
                            //TODO
                            //将交换后重复的题目去题库替换，取交换前后排序分更高的状态进入下一代
                            Map<String, Gene> map = new HashMap<String, Gene>();
                            for (Gene gene : offspring.getQChromosome()[getIndex].getLeg()) {
                                map.put(gene.getQuestion_id().toString(), gene);
                            }
                            //判断map对题目去重后是否和原题目数一致
                            //System.out.println("map" + map.size());
                            //System.out.println("offspring.getQChromosome()[getIndex].getLegLength()" + offspring.getQChromosome()[getIndex].getLegLength());
                            if (map.size() != offspring.getQChromosome()[getIndex].getLegLength()) {
                                //System.out.println("补充");
                                while (map.size() != offspring.getQChromosome()[getIndex].getLegLength()) {
                                    //System.out.println("while");
                                    int cha = (offspring.getQChromosome()[getIndex].getLegLength() - map.size());
                                    //System.out.println("差值 " + cha);
                                    for (int i = 0; i < cha; i++) {
                                        //System.out.println("库里取一道题");
                                        //库里取一道题
                                        for (Leg leg : demiurge.getQChromosome()) {
                                            if (leg.getTopic_id() == topic_id && leg.getType() == type) {
                                                Gene gene = offspring.getOne(leg);
                                                map.put(gene.getQuestion_id().toString(), gene);
                                                break;
                                            }
                                        }

                                    }
                                }
                                //将map里的gene放入子代染色体里
                                Gene[] gn = map.values().toArray(new Gene[parent1.getQChromosome()[getIndex].getLegLength()]);
                                for (int offset = 0; offset < parent1.getQChromosome()[getIndex].getLegLength(); offset++) {
                                    offspring.getQChromosome()[getIndex].setGene(offset, gn[offset]);
                                }
                            }

                            break;
                        }
                    }

                }
                //取交叉前后排序分高的题集进入下一代
                if (calcFitness(offspring, population.getMaster()) >= parent1.getFitness()) {
                    //add offspring to newPopulation
                    newPopulation.setIndividual(populationIndex, offspring);
                } else {
                    newPopulation.setIndividual(populationIndex, parent1);
                }
                //本次题型交叉完毕后退出循环染色体组

                //newPopulation.setIndividual(populationIndex, offspring);
            } else {
                //add individual to newPopulation without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }

        }

        return newPopulation;
    }

    public Population mutatePopulation(Population population, Map<String, String> typeCount, int topic_id, Table table, Demiurge demiurge){
        String type = typeCount.keySet().toArray(new String[typeCount.size()])[(int)Math.floor(Math.random()*typeCount.size())];
        //Initialize population
        Population newPopulation = new Population(this.populationSize);
        //Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
            //skip mutation if this is an elite individual
            if (populationIndex >= this.elitismCount) {
                //Loop individual's gene
                for (int getIndex = 0; getIndex < individual.getQchromosomeLength(); getIndex++) {
                    //当满足随机题型&&满足随机概率
                    if (this.mutationRate > Math.random() && individual.getQChromosome()[getIndex].getType() == type) {
                        //TODO
                        //从题库抽取一道题
                        //设置去重
                        Map<String, Gene> map = new HashMap<String, Gene>();
                        for (Gene gene : individual.getQChromosome()[getIndex].getLeg()) {
                            map.put(gene.getQuestion_id().toString(), gene);
                        }
                        for (Leg leg : demiurge.getQChromosome()) {
                            if (leg.getTopic_id() == topic_id && leg.getType() == type) {
                                Gene newGene = individual.getOne(leg);
                                //替换一题
                                //System.out.println("替换一题");
                                individual.getQChromosome()[getIndex].setGene((int) Math.floor(Math.random() * individual.getQChromosome()[getIndex].getLegLength()), newGene);
                                while (map.size() != individual.getQChromosome()[getIndex].getLegLength()) {
                                    int cha = (individual.getQChromosome()[getIndex].getLegLength() - map.size());
                                    //System.out.println("差值 " + cha);
                                    for (int i = 0; i < cha; i++) {
                                        //库里取一道题
                                        for (Leg leg2 : demiurge.getQChromosome()) {
                                            if (leg2.getTopic_id() == topic_id && leg2.getType() == type) {
                                                Gene gene = individual.getOne(leg);
                                                map.put(gene.getQuestion_id().toString(), gene);
                                                break;
                                            }
                                        }

                                    }
                                }
                                Gene[] gn = map.values().toArray(new Gene[individual.getQChromosome()[getIndex].getLegLength()]);
                                for (int offset = 0; offset < individual.getQChromosome()[getIndex].getLegLength(); offset++) {
                                    individual.getQChromosome()[getIndex].setGene(offset, gn[offset]);
                                }

                            }
                            break;
                        }

                    }
                }
                //int mutationIndex = (int) Math.floor(Math.random() * individual.getchromosomeLength());
            }
            newPopulation.setIndividual(populationIndex, individual);
        }
        return newPopulation;
    }
}
