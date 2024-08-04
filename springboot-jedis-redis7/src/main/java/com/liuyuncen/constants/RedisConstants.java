package com.liuyuncen.constants;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.constants
 * @author: Xiang想
 * @createTime: 2024-08-02  16:27
 * @description: TODO
 * @version: 1.0
 */
public class RedisConstants {
    public static String REDIS_IP = "";
    public static Integer REDIS_PORT = null;
    public static String REDIS_PASSWORD = "";


    public static String REDIS_IP_WIN = "192.168.111.36";
    public static Integer REDIS_PORT_WIN = 16301;
    public static String REDIS_PASSWORD_WIN = "123456";


    public static String REDIS_IP_MAC = "192.168.58.10";
    public static Integer REDIS_PORT_MAC = 16380;
    public static String REDIS_PASSWORD_MAC = "123456";


    static String className = RedisConstants.class.getName();

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            REDIS_IP = REDIS_IP_WIN;
            REDIS_PORT = REDIS_PORT_WIN;
            REDIS_PASSWORD = REDIS_PASSWORD_WIN;
        } else if (osName.contains("mac")) {
            REDIS_IP = REDIS_IP_MAC;
            REDIS_PORT = REDIS_PORT_MAC;
            REDIS_PASSWORD = REDIS_PASSWORD_MAC;
        }

        System.out.println("=====>【" + className + "】info: {IP:" + REDIS_IP + ",Port:" + REDIS_PORT + ",Auth:" + REDIS_PASSWORD + "}");
    }
}
