package com.toolbox.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private RequestBean(Integer code, String msg, T data)  {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 状态码 + 成功提示信息
     */
    public static <T> RequestBean<T> Success() {
        return new RequestBean<>(200,"success");
    }

    /**
     * 状态码 + 成功提示信息 + 数据
     */
    public static <T> RequestBean<T> Success( T data) throws JsonProcessingException {
        return new RequestBean<>(200, "success",data);
    }

    /**
     * 状态码 + 错误信息
     */
    public static <T> RequestBean<T> Error() {
        return new RequestBean<>(500, "error");
    }


}
