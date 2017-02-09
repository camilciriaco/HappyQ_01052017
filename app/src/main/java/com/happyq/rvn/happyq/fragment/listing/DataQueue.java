package com.happyq.rvn.happyq.fragment.listing;

/**
 * Created by RVN on 1/20/2017.
 */

public class DataQueue  {

    //Queueing Data list
    public String queuename;
    public String queuestatus;
    public String queueactivity;
    public String queuemaxreserve;

    //Registration Queueing Data list
    public String rqueuename;
    public String rqueuestime;
    public String rqueueetime;
    public String rqueueoperationday;

    //annpuncement
    public String Announcement;
    public String Atitle;

    //img
    public String banners;

    //queue_number
    public String cur;
    public String rec;
    public String ctr;


    public String getQueuename() {
        return queuename;
    }
    // setting  name
    public void setQueuename(String queuename){
        this.queuename =queuename;
    }

    public String getQueuestatus() {
        return queuestatus;
    }
    // setting  name
    public void setQueuestatus(String queuestatus){
        this.queuestatus =queuestatus;
    }



}
