package com.duzj.navigation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 链接信息修改记录
 * @TableName url_info_change_log
 */
@TableName(value ="url_info_change_log")
@Data
public class UrlInfoChangeLog implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 原 id
     */
    @TableField(value = "original_id")
    private Integer originalId;

    /**
     * 环境 id
     */
    @TableField(value = "environment_id")
    private Integer environmentId;

    /**
     * url名称
     */
    @TableField(value = "url_name")
    private String urlName;

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
     * 排序,数字越小展示越靠前
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}