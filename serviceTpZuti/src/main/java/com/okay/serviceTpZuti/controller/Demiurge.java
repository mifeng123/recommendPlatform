package com.okay.serviceTpZuti.controller;

import com.okay.serviceTpZuti.domain.Gene;
import com.okay.serviceTpZuti.domain.Leg;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Table;

import java.util.List;
import java.util.Map;

public class Demiurge {
    private Leg[] Qchromosome;

    public Demiurge (Map<String, String> typeCount, int topic_id, Table sf_question_score, Table sf_link_topic_question, Table sf_link_topic_cnt, List<String> wro_list, List<String> rit_list) {
        int legIndex = 0;
        this.Qchromosome = new Leg[typeCount.size()];
        for (String type : typeCount.keySet()) {
            String count = typeCount.get(type);
            Leg leg = fromHbase(type, topic_id, sf_question_score, sf_link_topic_question, sf_link_topic_cnt, wro_list, rit_list);
            //this.chromosome = new int[chromosomeLength];
            System.out.println(topic_id + " Individual : legIndex : " + legIndex);
            System.out.println(topic_id + " Individual : leg : " + leg.getTopic_id());
            this.setQLeg(legIndex, leg);
            legIndex++;
        }
    }

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

    //从hbase选取题目
    public Leg fromHbase(String type, int topic_id, Table sf_question_score, Table sf_link_topic_question, Table sf_link_topic_cnt, List<String> wro_list, List<String> rit_list) {
        //TODO
        System.out.println(topic_id + " fromHbase");
        int cnt = 500;
        Leg leg = new Leg(type, topic_id, cnt);
        Gene[] gn = leg.getLeg();
        //String top - topic_id.




//        hbaseConfig = HBaseConfiguration.create();
//        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
//        hbaseConfig.set("hbase.defaults.for.version.skip", "true");
//        //conf.set("zookeeper.znode.parent", "/hbase-unsecure")
//        hbaseConfig.set("hbase.zookeeper.quorum", "10.10.7.3");

        try {
            //取知识点下题量
            //System.out.println(topic_id + "_" + type);
            Get get3 = new Get(Bytes.toBytes(topic_id + "_" + type));
            Result result3 = sf_link_topic_cnt.get(get3);
            int geneIndex = 0;
            if (result3.size() > 0) {
                int qcnt = Integer.parseInt(Bytes.toString(result3.getValue(Bytes.toBytes("cnt"), Bytes.toBytes("cnt"))));
                //System.out.println(qcnt);
                for (int i = 1; i <= qcnt; i++) {
                    if (geneIndex < cnt) {
                        //顺序取题ID
                        //System.out.println(topic_id + "_" + type + "_" + i);
                        Get get1 = new Get(Bytes.toBytes((topic_id + "_" + type + "_" + i)));
                        Result result = sf_link_topic_question.get(get1);
                        String q = Bytes.toString(result.getValue(Bytes.toBytes("question_id"), Bytes.toBytes("question_id")));
                        //System.out.println(q);
                        String qtype = Bytes.toString(result.getValue(Bytes.toBytes("question_type_id"), Bytes.toBytes("question_type_id")));
                        //取题目属性
                        //过滤错题和对题
                        if (!wro_list.contains(q) && !rit_list.contains(q)) {
                            Get get2 = new Get(Bytes.toBytes((topic_id + "_" + qtype + "_" + q)));
                            if (sf_question_score.get(get2).size() > 0) {
                                Result result2 = sf_question_score.get(get2);
                                Gene gene = new Gene();
                                System.out.println(topic_id + "_" + type + "_" + q);
//                                System.out.println("question_id" + Long.parseLong(Bytes.toString(result2.getValue(Bytes.toBytes("question_id"), Bytes.toBytes("question_id")))));
//                                System.out.println(Integer.parseInt(Bytes.toString(result2.getValue(Bytes.toBytes("question_type_id"), Bytes.toBytes("question_type_id")))));
//                                System.out.println(Double.parseDouble(Bytes.toString(result2.getValue(Bytes.toBytes("score"), Bytes.toBytes("score")))));
//                                System.out.println(Long.parseLong(Bytes.toString(result2.getValue(Bytes.toBytes("topic_id"), Bytes.toBytes("topic_id")))));
//                                System.out.println(Integer.parseInt(Bytes.toString(result2.getValue(Bytes.toBytes("difficulty"), Bytes.toBytes("difficulty")))));
                                gene.setQuestion_id(Long.parseLong(Bytes.toString(result2.getValue(Bytes.toBytes("question_id"), Bytes.toBytes("question_id")))));
                                gene.setQuestion_type_id(Integer.parseInt(Bytes.toString(result2.getValue(Bytes.toBytes("question_type_id"), Bytes.toBytes("question_type_id")))));
                                gene.setScore(Double.parseDouble(Bytes.toString(result2.getValue(Bytes.toBytes("score"), Bytes.toBytes("score")))));
                                gene.setTopic_id(Long.parseLong(Bytes.toString(result2.getValue(Bytes.toBytes("topic_id"), Bytes.toBytes("topic_id")))));
                                gene.setDifficulty(Integer.parseInt(Bytes.toString(result2.getValue(Bytes.toBytes("difficulty"), Bytes.toBytes("difficulty")))));
                                gn[geneIndex] = gene;
                                geneIndex++;
                            }
                        }


                    } else {
                        break;
                    }
                }
            }
            leg.setMax(geneIndex);
            System.out.println(topic_id + " MAX " + geneIndex);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
        }

        return leg;
    }
}
