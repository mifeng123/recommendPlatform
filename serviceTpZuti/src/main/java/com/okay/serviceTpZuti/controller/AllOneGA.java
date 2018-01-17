package com.okay.serviceTpZuti.controller;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;



import java.util.*;

import java.util.Map;

public class AllOneGA {
//    private static Configuration hbaseConfig = null;
//    private static Map<String, String>  typeCount = null;
//    private static int topic_id = 0;
//    private static String row = null;
//    private static String master = null;
//
//    public static void  main(String[] agrs) {
//
//        hbaseConfig = HBaseConfiguration.create();
//        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
//        hbaseConfig.set("hbase.defaults.for.version.skip", "true");
//        //conf.set("zookeeper.znode.parent", "/hbase-unsecure")
//        hbaseConfig.set("hbase.zookeeper.quorum", "10.10.7.3,10.10.7.4,10.10.7.5,10.10.7.2,10.10.7.1");
//        typeCount = new HashMap<String, String>();
//        Map<String, String>  typeCount2 = new HashMap<String, String>();
//        //typeCount.put("1", "6");
//        //typeCount.put("2", "4");
//        //topic_id = 5841;
//        //master = "A";
//        System.out.println("123");
//
//        //jason
//        String j_str = "{\"teacher_id\":2,\"student_id\":[82951185593],\"topic_id\":5841,\"type\":1,\"question_type_count\":[{\"question_type\":1, \"question_count\":6},{\"question_type\":2, \"question_count\":4}],\"master\":\"A\",\"wrong_question\":[],\"basket_question\":[]}";
//        JSONObject jsonObject = JSONObject.fromObject(j_str);
//        //JsonObject jj = json.getAsJsonObject(j_str);
//        JSONArray tc = jsonObject.getJSONArray("question_type_count");
//        for (int i=0;i<tc.size();i++) {
//            String tyct = tc.get(i).toString();
//            JSONObject questionJsonObject = JSONObject.fromObject(tyct);
//            typeCount.put(questionJsonObject.get("question_type").toString(), questionJsonObject.get("question_count").toString());
//        }
//
//
//
//        try {
//            Connection conn = ConnectionFactory.createConnection(hbaseConfig);
//            TableName tableName = TableName.valueOf("canal.sf_question_score");
//            TableName tableName2 = TableName.valueOf("canal.sf_link_topic_question");
//            TableName tableName3 = TableName.valueOf("canal.sf_link_topic_cnt");
//            TableName tableName4 = TableName.valueOf("canal.sf_student_exercise_new");
//            TableName tableName5 = TableName.valueOf("canal.sf_student_exercise_new_cnt");
//            Table sf_question_score = conn.getTable(tableName);
//            Table sf_link_topic_question = conn.getTable(tableName2);
//            Table sf_link_topic_cnt = conn.getTable(tableName3);
//            Table sf_student_exercise_new = conn.getTable(tableName4);
//            Table sf_student_exercise_new_cnt = conn.getTable(tableName5);
//            List<String> single_wro_list = new ArrayList();
//            List<String> wro_list = new ArrayList();
//            List<String> rit_list = new ArrayList();
//            List stu_l = new ArrayList();
//
//            ArrayList<String> stu = new ArrayList<String>(jsonObject.getJSONArray("student_id"));
//            System.out.println(stu);
//            System.out.println(jsonObject.get("topic_id"));
//            topic_id = Integer.parseInt(jsonObject.get("topic_id").toString());
//            System.out.println(jsonObject.get("master"));
//            master = jsonObject.get("master").toString();
//            System.out.println(jsonObject.get("question_type_count"));
//            System.out.println(typeCount);
//            System.out.println(typeCount2);
//            JSONArray sst = jsonObject.getJSONArray("student_id");
//            for (int i=0;i<sst.size();i++) {
//                String sid = sst.get(i).toString();
//                stu_l.add(sid);
//            }
//            System.out.println("stu_l" + stu_l);
//
//
////            stu_l.add("82951185593");
////            stu_l.add("82951185594");
////            stu_l.add("82951192489");
//
//            int que_total = 0; //总体量
//            for (String k : typeCount.keySet()) {
//                que_total += Integer.parseInt(typeCount.get(k));
//            }
//
//            //Demiurge demiurge = null;
//            //map 题ID和学生ID， 错
//            Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
//            //map2 题ID和学生个数， 错
//            Map<Long, Integer> map2 = new HashMap<Long, Integer>();
//            //map 题ID和学生ID, 对
//            Map<Long, List<Long>> map3 = new HashMap<Long, List<Long>>();
//            //map2 题ID和学生个数， 对
//            Map<Long, Integer> map4 = new HashMap<Long, Integer>();
//            if (stu_l.size() > 1) {
//                //TODO
//                //hbase取错题
//                //多人
//                System.out.println("多人");
//                setlist(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr", map, map2, map3, wro_list, rit_list, que_total);
//
//
//            } else if (stu_l.size() == 1) {
//                //单人
//                System.out.println("单人");
//                if (single_wro_list.size() == 0) {
//                    //单人，未选错题，取30%
//                    System.out.println("单人，未选错题");
//                    setlist(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr", map, map2, map3, wro_list, rit_list, que_total);
//
//                } else if (single_wro_list.size() > 0) {
//                    //将单人已选错题 付给 错题表
//                    System.out.println("单人，已选错题");
//                    wro_list = single_wro_list;
//                    Leg[] leg = get_wrong(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr");
//                    for (String type : typeCount.keySet()) {
//
//                        for (Leg lg : leg) {
//                            if (lg.getType() == type && lg.getWrong() == 1) {
//                                ArrayList<QuestionGene> aleg = lg.getQleg();
//
//                                for (QuestionGene gene : aleg) {
//                                    if (!map3.keySet().contains(gene.getQuestion_id())) {
//                                        List<Long> ll = new ArrayList<Long>();
//                                        ll.add(gene.getStudent_id());
//                                        map3.put(gene.getQuestion_id(), ll);
//                                    } else {
//                                        map3.get(gene.getQuestion_id()).add(gene.getStudent_id());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                System.out.println("err");
//            }
//            //循环错题，取题型，减去typeCount中相应题型数量
//            for (String type : typeCount.keySet()) {
//                for (String qid : wro_list) {
//                    String key = topic_id + "_" + type + "_" + qid;
//                    System.out.println(key);
//                    Get get = new Get(Bytes.toBytes(key));
//                    if (sf_question_score.get(get).size() > 0) {
//                        typeCount.put(type, String.valueOf(Integer.parseInt(typeCount.get(type))-1));
//                    }
//                    System.out.println(typeCount);
//                }
//            }
//            //取对题，加到错题List里
//            //create Demiurge
//            Demiurge demiurge2 = new Demiurge(typeCount, topic_id, sf_question_score, sf_link_topic_question, sf_link_topic_cnt, wro_list, rit_list);
//            System.out.println("-----------------------------------------------------");
//            int psize = 20;
//            for (Leg leg : demiurge2.getQChromosome()) {
//                int aa = (int)Math.floor(leg.getMax()/Integer.parseInt(typeCount.get(leg.getType())));
//                if (aa < psize) {
//                    psize = aa;
//                }
//            }
//            //psize = 0题目不足
//            //TODO
//            //crerate GA object
//            GeneticAlgorithm ga = new GeneticAlgorithm(psize, 0.01, 0.95, 1);
//            System.out.println("psize" + psize);
//            //Initialize population
//            Population population = ga.initPopulation(typeCount, topic_id, sf_question_score, demiurge2, master);
//            System.out.println("2");
//            //Individual gdid = ga.evalPopulationMax(population, 20);
//            int generation = 1;
//
//            while (generation <= 100) {
//                //Apply crosssover
//                population = ga.crossoverPopulation(population, typeCount, topic_id, sf_question_score, demiurge2);
//                System.out.println("crosssover : " + generation);
//                //mutation
//                population = ga.mutatePopulation(population, typeCount, topic_id, sf_question_score, demiurge2);
//                System.out.println("mutation : " + generation);
//                ga.evalPopulation(population, master);
//
//                //
//                System.out.println("generation : " + generation);
//                generation++;
//            }
//            //输出最高
//            Individual gdid = population.getFittest(0);
//            List<String> outj = new ArrayList<String>();
//            for (Leg leg : gdid.getQChromosome()) {
//                for (Gene gene : leg.getLeg()) {
//                    System.out.println(gene.getQuestion_id());
//                    System.out.println(gene.getScore());
//                    String jin = "{question_id:" + gene.getQuestion_id() + ",score:" + gene.getScore() + "}";
//                    outj.add(jin);
//                }
//            }
//            String json = JSONArray.fromObject(outj).toString();
//            System.out.println(wro_list);
//            System.out.println(outj);
//            System.out.println(json);
//            sf_question_score.close();
//            sf_link_topic_question.close();
//            sf_link_topic_cnt.close();
//            sf_student_exercise_new.close();
//            sf_student_exercise_new_cnt.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println();
//        }
//    }
//
//    public static void setlist (Map<String, String> typeCount, int topic_id, Table sf_student_exercise_new, Table sf_student_exercise_new_cnt, List<String> stu_l, String wr, Map<Long, List<Long>> map, Map<Long, Integer> map2, Map<Long, List<Long>> map3, List<String> wro_list, List<String> rit_list, int que_total) {
//        System.out.println("setlist");
//        Leg[] leg = get_wrong(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr");
//        for (String type : typeCount.keySet()) {
//            System.out.println("type" + type);
//            for (Leg lg : leg) {
//                System.out.println("leg  " + lg.getType() + "  w " + lg.getWrong());
//                if (lg.getType() == type && lg.getWrong() == 2) {
//                    //多人， 错
//                    ArrayList<QuestionGene> aleg = lg.getQleg();
//
//                    for (QuestionGene gene : aleg) {
//                        if (!map.keySet().contains(gene.getQuestion_id())) {
//                            List<Long> ll = new ArrayList<Long>();
//                            ll.add(gene.getStudent_id());
//                            map.put(gene.getQuestion_id(), ll);
//                            System.out.println(gene.getQuestion_id() + ",  " + ll);
//                        } else {
//                            map.get(gene.getQuestion_id()).add(gene.getStudent_id());
//                        }
//                    }
//                } else if (lg.getType() == type && lg.getWrong() == 1) {
//                    //多人， 对
//                    ArrayList<QuestionGene> aleg = lg.getQleg();
//
//                    for (QuestionGene gene : aleg) {
//                        if (!map3.keySet().contains(gene.getQuestion_id())) {
//                            List<Long> ll = new ArrayList<Long>();
//                            ll.add(gene.getStudent_id());
//                            map3.put(gene.getQuestion_id(), ll);
//                        } else {
//                            map3.get(gene.getQuestion_id()).add(gene.getStudent_id());
//                        }
//                    }
//                }
//            }
//        }
//        for (Long id : map.keySet()) {
//            map2.put(id, map.get(id).size());
//            System.out.println("map2错，" + id);
//        }
//        Map<Long, Integer> resultMap = sortMapByValue(map2);
//        int wcnt = (int)Math.floor(que_total*0.3);
//        //add wro_list
//        for (Map.Entry<Long, Integer> entry : resultMap.entrySet()) {
//            if (wcnt > 0) {
//                wro_list.add(entry.getKey().toString());
//                wcnt--;
//            }
//        }
//        //add rti_list
//        for (Long id : map3.keySet()) {
//            rit_list.add(id.toString());
//        }
//    }
//
//    public void wrong_que(Map<String, String> typeCount, int topic_id, Table sf_student_exercise_new, Table sf_student_exercise_new_cnt, List<String> stu_l, List<String> que_l, String wr) {
//        int que_total = 0;
//        for (String k : typeCount.keySet()) {
//            que_total += Integer.parseInt(typeCount.get(k));
//        }
////        if (stu_l.size() > 1) {
////            //map 题ID和学生ID
////            Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
////            Leg[] leg = this.get_wrong(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr");
////            for (String type : typeCount.keySet()) {
////
////                for (Leg lg : leg) {
////                    if (lg.getType() == type && lg.getWrong() == 2) {
////                        ArrayList<QuestionGene> aleg = lg.getQleg();
////
////                        for (QuestionGene gene : aleg) {
////                            if (!map.keySet().contains(gene.getQuestion_id())) {
////                                List<Long> ll = new ArrayList<Long>();
////                                ll.add(gene.getStudent_id());
////                                map.put(gene.getQuestion_id(), ll);
////                            } else {
////                                map.get(gene.getQuestion_id()).add(gene.getStudent_id());
////                            }
////                        }
////                    }
////                }
////            }
////            //map2 题ID和学生个数
////            Map<Long, Integer> map2 = new HashMap<Long, Integer>();
////            for (Long id : map.keySet()) {
////                map2.put(id, map.get(id).size());
////            }
////            Map<Long, Integer> resultMap = this.sortMapByValue(map2);
////        } else if (stu_l.size() == 1) {
////            if (que_l.size() == 0) {
////                Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
////                Leg[] leg = this.get_wrong(typeCount, topic_id, sf_student_exercise_new, sf_student_exercise_new_cnt, stu_l, "wr");
////                for (String type : typeCount.keySet()) {
////
////                    for (Leg lg : leg) {
////                        if (lg.getType() == type && lg.getWrong() == 2) {
////                            ArrayList<QuestionGene> aleg = lg.getQleg();
////
////                            for (QuestionGene gene : aleg) {
////                                if (!map.keySet().contains(gene.getQuestion_id())) {
////                                    List<Long> ll = new ArrayList<Long>();
////                                    ll.add(gene.getStudent_id());
////                                    map.put(gene.getQuestion_id(), ll);
////                                } else {
////                                    map.get(gene.getQuestion_id()).add(gene.getStudent_id());
////                                }
////                            }
////                        }
////                    }
////                }
////                //map2 题ID和学生个数
////                Map<Long, Integer> map2 = new HashMap<Long, Integer>();
////                for (Long id : map.keySet()) {
////                    map2.put(id, map.get(id).size());
////                }
////                Map<Long, Integer> resultMap = this.sortMapByValue(map2);
////            } else if (que_l.size() > 0) {
////
////                Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
////                List<Long> que_l2 = null;
////                for (String q : que_l) {
////                    que_l2.add(Long.parseLong(q));
////                }
////                map.put(Long.parseLong(stu_l.get(0)), que_l2);
////                Map<Long, Integer> map2 = new HashMap<Long, Integer>();
////
////            }
////        }
//    }
//
//    public static  Leg[]  get_wrong(Map<String, String> typeCount, int topic_id, Table sf_student_exercise_new, Table sf_student_exercise_new_cnt, List<String> stu_l, String wr) {
//        //对错一起取。分开算
//        //此方法可取对或错题
//        //wr  对错 1对，2错
//        //int cnt = 0;
//        //row_key : topicid_type_studentid_wrong_queid_反向时间戳
//        //您可以将date字段(Long类型)进行字节按位取反
//        System.out.println("get_wrong");
//        Leg[] lg = new Leg[typeCount.size()*2];
//        int i = 0;
//        for (String type : typeCount.keySet()) {
//            int cnt = Integer.parseInt(typeCount.get(type));
//
//            Leg wrong_leg = new Leg(type, topic_id, 2, "w");
//            Leg right_leg = new Leg(type, topic_id, 1, "r");
//            ArrayList<QuestionGene> wgn = wrong_leg.getQleg();
//            ArrayList<QuestionGene> rgn = right_leg.getQleg();
//            try {
//                for (String student_id : stu_l) {
//                    int geneIndex = 1;
//                    String key1 = topic_id + "_" + student_id + "_" + "2" + "_" + type;
//                    System.out.println(key1);
//                    Get get1 = new Get(Bytes.toBytes(key1));
//                    Result result1 = sf_student_exercise_new_cnt.get(get1);
//                    if (result1.size() > 0) {
//                        System.out.println(result1.size());
//                        int qcnt = Integer.parseInt(Bytes.toString(result1.getValue(Bytes.toBytes("cnt"), Bytes.toBytes("cnt"))));
//                        System.out.println("wcnt" + qcnt);
//                        for (int k = geneIndex; k <= qcnt; k++) {
//                            QuestionGene gene = new QuestionGene();
//                            String key = topic_id + "_" + student_id + "_" + "2" + "_" + type + "_" + geneIndex;
//                            System.out.println(key);
//                            Get get = new Get(Bytes.toBytes(key));
//                            Result result = sf_student_exercise_new.get(get);
//                            //ddd 1630 1538
//                            if (result.size() > 0) {
//                                gene.setQuestion_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("question_id"), Bytes.toBytes("question_id")))));
//                                gene.setQuestion_type_id(Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("question_type_id"), Bytes.toBytes("question_type_id")))));
//                                gene.setStudent_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("student_id"), Bytes.toBytes("student_id")))));
//                                gene.setTopic_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("topic_id"), Bytes.toBytes("topic_id")))));
//                                gene.setWrong(Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("wrong"), Bytes.toBytes("wrong")))));
//                                System.out.println(gene.getQuestion_id());
//                                System.out.println(gene.getStudent_id());
//                                System.out.println(gene.getQuestion_type_id());
//                                System.out.println(gene.getTopic_id());
//                                System.out.println(gene.getWrong());
//                                wgn.add(gene);
//                                geneIndex++;
//                            } else {
//                                break;
//                            }
//                        }
//                    }
//
//                    int geneIndex_r = 1;
//                    String row = topic_id + "_" + student_id + "_" + "1" + "_" + type;
//                    Get g1 = new Get(Bytes.toBytes(row));
//                    Result r1 = sf_student_exercise_new_cnt.get(g1);
//                    if (r1.size() > 0) {
//                        int qcnt2 = Integer.parseInt(Bytes.toString(r1.getValue(Bytes.toBytes("cnt"), Bytes.toBytes("cnt"))));
//                        for (int k = geneIndex_r; k <= qcnt2; k++) {
//                            QuestionGene gene = new QuestionGene();
//                            String key = topic_id + "_" + student_id + "_" + "1" + "_" + type + "_" + geneIndex_r;
//                            Get get = new Get(Bytes.toBytes(key));
//                            Result result = sf_student_exercise_new.get(get);
//                            if (result.size() > 0) {
//                                //ddd 1630 1538
//                                gene.setQuestion_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("question_id"), Bytes.toBytes("question_id")))));
//                                gene.setQuestion_type_id(Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("question_type_id"), Bytes.toBytes("question_type_id")))));
//                                gene.setStudent_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("student_id"), Bytes.toBytes("student_id")))));
//                                gene.setTopic_id(Long.parseLong(Bytes.toString(result.getValue(Bytes.toBytes("topic_id"), Bytes.toBytes("topic_id")))));
//                                gene.setWrong(Integer.parseInt(Bytes.toString(result.getValue(Bytes.toBytes("wrong"), Bytes.toBytes("wrong")))));
//                                rgn.add(gene);
//                                geneIndex_r++;
//                            } else {
//                                break;
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println();
//            }
//            lg[i] = wrong_leg;
//            i++;
//            lg[i] = right_leg;
//            i++;
//        }
//
//
//        return lg;
//
//    }
//
//    public static Map<Long, Integer> sortMapByValue(Map<Long, Integer> oriMap) {
//        System.out.println("sortMapByValue");
//        if (oriMap == null || oriMap.isEmpty()) {
//            return null;
//        }
//        Map<Long, Integer> sortedMap = new LinkedHashMap<Long, Integer>();
//        List<Map.Entry<Long, Integer>> entryList = new ArrayList<Map.Entry<Long, Integer>>(
//                oriMap.entrySet());
//        Collections.sort(entryList, new MapValueComparator());
//
//        Iterator<Map.Entry<Long, Integer>> iter = entryList.iterator();
//        Map.Entry<Long, Integer> tmpEntry = null;
//        while (iter.hasNext()) {
//            tmpEntry = iter.next();
//            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//        }
//        return sortedMap;
//    }
}
