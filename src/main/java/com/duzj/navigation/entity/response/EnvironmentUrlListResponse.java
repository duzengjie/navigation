package com.duzj.navigation.entity.response;

import com.duzj.navigation.entity.UrlInfo;
import lombok.Data;

import java.util.List;

@Data
public class EnvironmentUrlListResponse {

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
