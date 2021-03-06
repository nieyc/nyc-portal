package com.github.nyc.util;

import org.springframework.stereotype.Service;

/**
 * 传递经纬度返回差距 单位（米）
 * @author nyc
 *
 */

@Service
public class DistanceUtils {
	
	private static final double EARTH_RADIUS = 6378137;  
	  
    private static double rad(double d)  
    {  
        return d * Math.PI / 180.0;  
    }  
  
    /** 
     * 传递经纬度返回差距 单位（米） 
     * @param lat1 经度1 
     * @param lng1 纬度1 
     * @param lat2 经度2 
     * @param lng2 纬度2 
     * @return 
     */  
    public static double getDistance(double lat1, double lng1, double lat2, double lng2)  
    {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
        double a = radLat1 - radLat2;  
        double b = rad(lng1) - rad(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +  
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        return s;  
    }  
    
    public static void main(String[] args) {  
        System.out.println(DistanceUtils.getDistance(29.490295,106.486654,29.615467,106.581515));  
    } 

}
