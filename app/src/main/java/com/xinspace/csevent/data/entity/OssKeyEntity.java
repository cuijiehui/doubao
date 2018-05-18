package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * oss授权值
 */
public class OssKeyEntity implements Serializable{

    private String AccessKeySecret;
    private String AccessKeyId;
    private String Expiration;
    private String SecurityToken;


    public OssKeyEntity() {
    }

    public OssKeyEntity(String accessKeySecret, String accessKeyId, String expiration, String securityToken) {
        AccessKeySecret = accessKeySecret;
        AccessKeyId = accessKeyId;
        Expiration = expiration;
        SecurityToken = securityToken;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public String getExpiration() {
        return Expiration;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    @Override
    public String toString() {
        return "OssKeyEntity{" +
                "AccessKeySecret='" + AccessKeySecret + '\'' +
                ", AccessKeyId='" + AccessKeyId + '\'' +
                ", Expiration='" + Expiration + '\'' +
                ", SecurityToken='" + SecurityToken + '\'' +
                '}';
    }
}
