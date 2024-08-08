package com.liuyuncen.controller;

import com.liuyuncen.service.GeoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述： geo案例
 *
 * @author: Xiang
 * @date: 2024年08月08日 23:16:40
 * @Description:
 */
@Api(tags = "地图位置附近推送GEO")
@RestController
public class GeoController {

    @Autowired
    private GeoService geoService;

    @ApiOperation("添加坐标 geoadd")
    @PostMapping("/geoadd")
    public String geoAdd(){
        return geoService.getAdd();
    }

    @ApiOperation("获取经纬坐标 geopos")
    @GetMapping("/geopos")
    public Point position(String member){
        return geoService.position(member);
    }

    @ApiOperation("获取经纬度base32编码 geohash")
    @GetMapping("/geohash")
    public String hash(String member){
        return geoService.hash(member);
    }

    @ApiOperation("获取两个给定位置之间距离")
    @GetMapping("/geodist")
    public Distance distance(String member1,String member2){
        return geoService.distance(member1,member2);
    }

    @ApiOperation("通过经纬度查找北京王府井附近的")
    @GetMapping("/georadius")
    public GeoResults radiusByxy(){
        return geoService.radiusByxy();
    }

    @ApiOperation("通过地方查找附近，本案例以天安门为当前地址")
    @GetMapping("/georadiusByMember")
    public GeoResults radiusByMember(){
        return geoService.radiusByMember();
    }

}
