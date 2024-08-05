package com.liuyuncen.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.entities
 * @author: Xiang想
 * @createTime: 2024-08-05  14:34
 * @description: TODO
 * @version: 1.0
 */

@Data
@Table("r_user")
public class User {

    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private String city;
}
