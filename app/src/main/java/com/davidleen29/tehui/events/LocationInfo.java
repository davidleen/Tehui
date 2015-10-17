package com.davidleen29.tehui.events;
/**
 * Created by davidleen29 on 2015/7/24.
 */
public class LocationInfo {

    public String  cityName;
    public double x;
    public double y;
    public String coorType;
    public LocationInfo(String cityName, double x, double y,String coorType) {
        this.cityName = cityName;
        this.x = x;
        this.y = y;
        this.coorType=coorType;
    }
}
