package com.liuyuncen.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 create table t_inventory(
 id bigint NOT NULL AUTO_INCREMENT,
 sale int(11),
 uuid varchar(50),
 type varchar(50),
 succ varchar(50),
 PRIMARY KEY (`id`)
 )
 *
 * @author: Xiang
 * @date: 2024年08月12日 00:20:03
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("t_inventory")
public class Inventory {


    @Id(keyType = KeyType.Auto)
    private Long id;
    @ApiModelProperty(value = "剩余件数")
    private Integer sale;
    @ApiModelProperty(value = "消费客户端")
    private String uuid;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "是否成功")
    private String succ;
}
