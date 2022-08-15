package com.github.mrzhqiang.rowing.system.init;

import lombok.Data;

@Data
public class SysInitData {

    private String id;
    private String type;
    private String name;
    private String status;
    private String startTime;
    private String endTime;
    private String errorMessage;
    private String errorStack;
}
