package com.jxj.jdoctorassistant.model;

/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class SearchWord {
    public static final String SEARCHWORD="SEARCHWORD";
    public static final String DATE="DATE";
    public static final String COUNT="COUNT";

    private String searchWord;
    private String date;
    private int count;

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }
}
