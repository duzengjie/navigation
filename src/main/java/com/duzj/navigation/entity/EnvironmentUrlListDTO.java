package com.duzj.navigation.entity;

import lombok.Data;

import java.util.List;

@Data
public class EnvironmentUrlListDTO {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 环境名称
     */
    private String name;

    /**
     * url信息
     */
    List<UrlInfo> data;
}
