package com.toolbox.domain;

import lombok.Data;

@Data
public class User {
    private  int ID;
    private  String UserName;
    private  String Password;
    private  String Salt;
    private  String Label;


}
