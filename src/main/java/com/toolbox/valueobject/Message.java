package com.toolbox.valueobject;

import lombok.Data;

/***
 * 请求/响应的消息对象；
 */
@Data
public class Message {
    private String message;

    public Message(String message) {
        this.message = message;
    }
}

