package com.toolbox.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;


@Data
public class RequestBean<T> {


    private Integer code;
    private String msg;
    private T data;


    private RequestBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private RequestBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = JSON.toJSONdata;
    }

    /**
     * 状态码 + 成功提示信息
     */
    public static <T> RequestBean<T> Success(String msg) {
        return new RequestBean<>(200,msg);
    }

    /**
     * 状态码 + 成功提示信息 + 数据
     */
    public static <T> RequestBean<T> Success(String msg, T data) {
        return new RequestBean<>(200, msg, data);
    }

    /**
     * 状态码 + 错误信息
     */
    public static <T> RequestBean<T> Error(String msg) {
        return new RequestBean<>(500, msg);
    }


}
