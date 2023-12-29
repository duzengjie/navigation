package com.duzj.navigation.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UrlInfoExcelDTO {

    /**
     * 自增主键
     */
    @ExcelProperty("编号")
    private Integer id;

    /**
     * 环境 id
     */
    @ExcelProperty("环境编号")
    private Integer environmentId;

    /**
     * 地址
     */
    @ExcelProperty("链接")
    private String url;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    private Date updateTime;

    /**
     * url名称
     */
    @ExcelProperty("链接名称")
    private String urlName;

}
