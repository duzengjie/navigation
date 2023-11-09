package com.duzj.navigation.entity.request;

import lombok.Data;

/**
 * @Description
 * @Date 2023/11/9 15:33
 * @Created by duzengjie
 */
@Data
public class EnvironmentInfoRequest {
    /**
     * 环境 id
     */
    private Integer id;
    /**
     * 环境名称
     */
    private String name;

}
