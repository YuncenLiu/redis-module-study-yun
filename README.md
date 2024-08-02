# Redis 系统学习笔记

通过继承 SpringBoot 实现各类业务，加强对 Redis 的掌握

项目构建于 [Bilibili 阳哥](https://www.bilibili.com/video/BV13R4y1v7sP) 特此感谢！

### Jedis 最早版本的 Redis 连接工具

[Jedis 操作 Redis 服务 代码案例](springboot-jedis-redis7/src/main/java/com/liuyuncen/demo/jedis/JedisDemo.java)

### 后续使用 Lettuce(生菜🥬)

Jedis 和 Lettuce 区别：Jedis 是单线程的，而 Lettuce 是多线程的。 SpringBoot 2.0 之后，默认使用 Lettuce, 是基于 Netty 的，性能更高。

[Lettuce 操作 Redis 服务 代码案例](springboot-jedis-redis7/src/main/java/com/liuyuncen/demo/lettuce/LettuceDemo.java)


### RedisTemplate【掌握】

RedisTemplate 自带一个 `lettuce-core` 所以，需要屏蔽或注释

#### 默认 RedisTemplate 接口会导致写入到 Redis 的 key 存在乱码问题，使用 StringRedisTemplate 解决