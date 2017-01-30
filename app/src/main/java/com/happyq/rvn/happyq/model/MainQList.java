package com.happyq.rvn.happyq.model;


public class MainQList {

    private String name, title , number;
    private int pic,rank;

    public MainQList(String name, String title, int rank, int pic){

        this.name = name;
        this.title = title;
        this.rank = rank;
        this.pic = pic;

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getSinger() {
        return title;
    }

    public void setTitle(String singer) {
        this.title = number;
    }

    public String getYear() {
        return number;
    }

    public void setNumber(String year) {
        this.number = number;
    }
}
