package com.jxj.jdoctorassistant.bean;

public class CommentBean {

    /**
     * EvaluationId : 25
     * Evaluation : 好好好
     * AddDate : 2018-10-26 13:20:44
     * Score : 0
     * CName : kevin
     * Anonymous : false
     * Type : 0
     */

    private int EvaluationId;
    private String Evaluation;
    private String AddDate;
    private int Score;
    private String CName;
    private boolean Anonymous;
    private int Type;

    public int getEvaluationId() {
        return EvaluationId;
    }

    public void setEvaluationId(int EvaluationId) {
        this.EvaluationId = EvaluationId;
    }

    public String getEvaluation() {
        return Evaluation;
    }

    public void setEvaluation(String Evaluation) {
        this.Evaluation = Evaluation;
    }

    public String getAddDate() {
        return AddDate;
    }

    public void setAddDate(String AddDate) {
        this.AddDate = AddDate;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public boolean isAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(boolean Anonymous) {
        this.Anonymous = Anonymous;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }
}
