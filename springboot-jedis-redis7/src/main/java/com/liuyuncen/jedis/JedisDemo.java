package com.liuyuncen.jedis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.demo
 * @author: Xiang想
 * @createTime: 2024-08-02  15:54
 * @description: TODO
 * @version: 1.0
 */
public class JedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.58.10", 16380);
        jedis.auth("123456");
        System.out.println(jedis.ping());

        Set<String> keys = jedis.keys("*");
        System.out.println("keys 所有的 Key = " + keys);

        jedis.set("k4", "hello-jedis");
        System.out.println("k4 = " + jedis.get("k4"));

        jedis.lpush("list", "11", "12", "13");
        List<String> list = jedis.lrange("list", 0, -1);
        System.out.println("list = " + list);

        jedis.sadd("set", "0", "2", "4");
        Set<String> set = jedis.smembers("set");
        System.out.println("set = " + set);
        jedis.srem("set", "2");
        System.out.println("set 删除元素 2 之后剩余元素个数" + jedis.smembers("set").size());

        jedis.hset("person","username","张三");
        System.out.println("person = " + jedis.hget("person","username"));

        Map<String,String> map = new HashMap<>();
        map.put("phone","198xxxx6253");
        map.put("address","beijing");
        map.put("mail","xiang@mail.com");
        jedis.hmset("map",map);
        List<String> hmget = jedis.hmget("map", "phone", "address", "mail");
        System.out.println("map = " + hmget);

        jedis.zadd("zset",60d,"v1");
        jedis.zadd("zset",70d,"v2");
        jedis.zadd("zset",80d,"v3");
        jedis.zadd("zset",90d,"v4");
        List<String> zset = jedis.zrange("zset", 0, -1);
        System.out.println("zset = " + zset);

        jedis.close();
    }
}
