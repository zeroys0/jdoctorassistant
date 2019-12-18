package com.jxj.jdoctorassistant.main.community.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/26.
 */

public class SelfHelpAbility {
    private String pageId;
    private String status;

    private String title;

    //private ArrayList<> quesitons;


    void initDate(){

        Answer a_one=new Answer();
        a_one.setAnswerId("0");
        a_one.setAnswer_content("男");
        a_one.setAns_state(0);
        Answer a_two=new Answer();
        a_two.setAnswerId("1");
        a_two.setAnswer_content("女");
        a_two.setAns_state(0);

        Answer a_three=new Answer();
        a_three.setAnswerId("0");
        a_three.setAnswer_content("男");
        a_three.setAns_state(0);
        Answer a_four=new Answer();
        a_four.setAnswerId("1");
        a_four.setAnswer_content("女");
        a_four.setAns_state(0);


        ArrayList<Answer> answers_one=new ArrayList<Answer>();
        answers_one.add(a_one);
        answers_one.add(a_two);

        ArrayList<Answer> answers_two=new ArrayList<Answer>();
        answers_two.add(a_one);
        answers_two.add(a_two);
        answers_two.add(a_three);
        answers_two.add(a_four);

        Quesition q_one=new Quesition();
        q_one.setQuesitionId("00");
        q_one.setType("0");
        q_one.setContent("1、您的性别：");
        q_one.setAnswers(answers_one);
        q_one.setQue_state(0);

        Quesition q_two=new Quesition();
        q_two.setQuesitionId("01");
        q_two.setType("1");
        q_two.setContent("2、您是党员吗？");
        q_two.setAnswers(answers_two);
        q_two.setQue_state(0);


    }

}
