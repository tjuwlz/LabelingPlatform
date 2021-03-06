package com.labeling.demo.entity;

public enum RespStatus {
    SUCCESS("success", 200),
    Over("标注任务已完成！", 201),  //标注结束

    Error("error", 400),
    InvalidFormat("请上传正确格式的文件！", 401),
    DuplicateTask("该任务已存在！", 402),
    UNAUTHEN("用户名或密码错误!!!", 403),
    OFFLINE("offline", 444),
    ;
    private String statusValue;
    private Integer statusCode;

    RespStatus(String statusValue, Integer statusCode) {
        this.statusValue = statusValue;
        this.statusCode = statusCode;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
