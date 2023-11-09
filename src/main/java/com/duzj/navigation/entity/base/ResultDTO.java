package com.duzj.navigation.entity.base;

import lombok.Data;

@Data
public class ResultDTO<T> {

    private String code;
    private String msg;
    private Long total;
    private T data;

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<T>(data);
    }

    public static <T> ResultDTO<T> success(T data, Long total) {
        return new ResultDTO<T>(data, total);
    }

    public static <T> ResultDTO<T> error(String code,String msg) {
        return new ResultDTO<T>(code,msg);
    }

    private ResultDTO(T data) {
        this.code = "200";
        this.msg = "success";
        this.data = data;
    }

    private ResultDTO(T data, Long total) {
        this.code = "200";
        this.msg = "success";
        this.data = data;
        this.total = total;
    }

    private ResultDTO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
