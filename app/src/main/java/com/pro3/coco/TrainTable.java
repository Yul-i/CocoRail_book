package com.pro3.coco;

public class TrainTable {
    String station;
    String time;

    public TrainTable(){

    }

    public TrainTable(String station, String time) {
        this.station = station;
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TrainTable{" +
                "station='" + station + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
