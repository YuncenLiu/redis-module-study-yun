package com.liuyuncen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeoService {

    public static final String CITY = "CITY";
    @Autowired
    private RedisTemplate redisTemplate;

    public String getAdd() {
        Map<String, Point> map = new HashMap<>();
        map.put("work",new Point(116.484281,39.938513));
        map.put("home",new Point(116.492089,39.995463));
        map.put("nene",new Point(116.507661,39.999771));
        redisTemplate.opsForGeo().add(CITY,map);
        return map.toString();
    }

    public Point position(String member) {
        List<Point> position = redisTemplate.opsForGeo().position(CITY, member);
        return position.get(0);
    }

    public String hash(String member) {
        List<String> hash = redisTemplate.opsForGeo().hash(CITY, member);
        return hash.get(0);
    }


    /**
     * 功能描述： 获取两个给定位置之间的距离
     *
     * @author: Xiang
     * @date: 2024年08月08日 23:36:12
     * @Description:
     * @param member1 第一个定位
     * @param member2 第二个定位
     * @return org.springframework.data.geo.Distance
     */
    public Distance distance(String member1, String member2) {
        Distance distance = redisTemplate.opsForGeo().distance(CITY, member1, member2, RedisGeoCommands.DistanceUnit.MILES);
        return distance;
    }

    public GeoResults radiusByxy() {
        // 望京南 116.489148,39.99073
        Circle circle = new Circle(116.489148, 39.99073, Metrics.MILES.getMultiplier());
        // 返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands
                .GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortDescending().limit(50);
        GeoResults radius = redisTemplate.opsForGeo().radius(CITY, circle, args);
        return radius;
    }

    public GeoResults radiusByMember() {
        return null;
    }
}
