package com.yulu.zhaoxinpeng.mytreasuremap.treasure;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/5.
 * 获取宝藏时的响应体数据 ResponeBody
 */
//实现序列化的对象（类）可以利用 intent.putExtra() 传递
public class Treasure implements Serializable{

    /**
     * htid : 173
     * htpoi : 北京市大兴区福东街
     * htsize : 0
     * htlevels : 0
     * htsc :
     * htxline : 116.64923659725756
     * htyline : 39.632932212422205
     * htheight : 4.9E-324
     * httid : 171
     * htcreatetime : 2016-04-29T15:12:11.596
     * htuserid : 168
     * htisstates : false
     * tdid : 171
     * tdtname : 老虎
     * tdtstate : false
     */

    @SerializedName("htpoi")
    private String location;
    //宝物大小
    @SerializedName("htsize")
    private int size;
    //寻找难度
    @SerializedName("htlevels")
    private int level;
    //经度
    @SerializedName("htxline")
    private double longitude;
    //纬度
    @SerializedName("htyline")
    private double latitude;
    @SerializedName("htheight")
    private double altitude;
    @SerializedName("tdid")
    private int id;
    @SerializedName("tdtname")
    private String title;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
