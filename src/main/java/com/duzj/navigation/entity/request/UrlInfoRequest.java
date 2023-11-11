package com.duzj.navigation.entity.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Description
 * @Date 2023/11/9 15:43
 * @Created by duzengjie
 */
@Data
public class UrlInfoRequest {
    private Integer id;

    /**
     * 环境 id
     */
    private Integer environmentId;

    /**
     * 地址
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * url名称
     */
    private String urlName;
}
