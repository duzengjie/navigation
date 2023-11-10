package com.duzj.navigation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 链接信息
 * @TableName url_info
 */
@TableName(value ="url_info")
@Data
public class UrlInfo implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 环境 id
     */
    @TableField(value = "environment_id")
    private Integer environmentId;

    /**
     * 地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * url名称
     */
    @TableField(value = "url_name")
    private String urlName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}