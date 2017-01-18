package com.example.infiny.mylocationtrackeradmin.Models;

import java.io.Serializable;

/**
 * Created by infiny on 16/1/17.
 */

public class Target implements Serializable{
    String date_str,avg_hr;

    public String getDate_str() {
        return date_str;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public String getAvg_hr() {
        return avg_hr;
    }

    public void setAvg_hr(String avg_hr) {
        this.avg_hr = avg_hr;
    }
}
