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

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("链接名称")
    private String urlName;

    @ExcelProperty("使用次数")
    private Integer useNum;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新时间")
    private Date updateTime;
}
