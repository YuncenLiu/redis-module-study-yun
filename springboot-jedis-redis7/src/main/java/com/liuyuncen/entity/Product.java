package com.liuyuncen.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "聚划算活动producte信息")
public class Product {

    @ApiParam("产品ID")
    private Long id;
    @ApiParam("产品名称")
    private String name;
    @ApiParam("产品价格")
    private Integer price;
    @ApiParam("产品详情")
    private String detail;
}
