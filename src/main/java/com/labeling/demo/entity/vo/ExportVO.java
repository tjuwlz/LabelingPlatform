package com.labeling.demo.entity.vo;

import java.io.Serializable;
import java.util.Date;

public class ExportVO implements Serializable {
    private Long instanceId;  //数据编号
    private String raw;       //原始数据
    private String expertTag; //专家标签
    private String userName;    //标注者
    private String userTag;  //用户标签
    private String userType;     //用户类型：专家或普通用户
    private Date tagTime;   //标注时间
    private Float responseTime;  //标注响应时间

    public ExportVO() {
        super();
    }

    public ExportVO(Long instanceId, String raw, String expertTag, String userName, String userTag, String userType, Date tagTime, Float responseTime) {
        this.instanceId = instanceId;
        this.raw = raw;
        this.expertTag = expertTag;
        this.userName = userName;
        this.userTag = userTag;
        this.userType = userType;
        this.tagTime = tagTime;
        this.responseTime = responseTime;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getExpertTag() {
        return expertTag;
    }

    public void setExpertTag(String expertTag) {
        this.expertTag = expertTag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getTagTime() {
        return tagTime;
    }

    public void setTagTime(Date tagTime) {
        this.tagTime = tagTime;
    }

    public Float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Float responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "ExportVO{" +
                "raw='" + raw + '\'' +
                ", expertTag='" + expertTag + '\'' +
                ", userName='" + userName + '\'' +
                ", userTag='" + userTag + '\'' +
                ", tagTime=" + tagTime +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        } else if(this == obj) {  //先比较hashCode，不同时再分别比较属性
            return true;
        } else if(obj instanceof ExportVO){
            ExportVO objOther = (ExportVO) obj;
            return (raw.equals(objOther.getRaw()) && userName.equals(objOther.getUserName()) && expertTag.equals(objOther.getExpertTag()) && userTag.equals(objOther.getUserTag()));
        }

        return false;
    }
}
