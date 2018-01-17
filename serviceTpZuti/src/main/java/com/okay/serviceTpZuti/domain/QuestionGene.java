package com.okay.serviceTpZuti.domain;

public class QuestionGene {
    private Long question_id;
    private Long student_id;
    private  int wrong;
    private  int question_type_id;
    private Long topic_id;
    private int total_num;
    private double ratio;
    private int pub_cnt;
    private double avg_time;
    private String analysis;
    private  String topic_name;

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getPub_cnt() {
        return pub_cnt;
    }

    public void setPub_cnt(int pub_cnt) {
        this.pub_cnt = pub_cnt;
    }

    public double getAvg_time() {
        return avg_time;
    }

    public void setAvg_time(double avg_time) {
        this.avg_time = avg_time;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getQuestion_type_id() {
        return question_type_id;
    }

    public void setQuestion_type_id(int question_type_id) {
        this.question_type_id = question_type_id;
    }

    public Long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Long topic_id) {
        this.topic_id = topic_id;
    }
}
