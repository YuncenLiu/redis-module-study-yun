package com.liuyuncen.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 create table t_customer(
 id bigint NOT NULL AUTO_INCREMENT,
 cname varchar(40),
 age int(3),
 phone varchar(11),
 sex tinyint(2),
 birth timestamp,
 PRIMARY KEY (`id`)
 )
 * </span>
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.entity
 * @author: Xiang想
 * @createTime: 2024-08-09  11:54
 * @description: TODO
 * @version: 1.0
 */
@Data
@Table("t_customer")
public class Customer {

    @Id(keyType = KeyType.Auto)
    private Long id;
    private String cname;
    private Integer age;
    private String phone;
    private Byte sex;
    private Date birth;
}
