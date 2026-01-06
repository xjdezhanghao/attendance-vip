package com.attendance.framework.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wxwork")
public class WxWorkConfig {

   // private String access_token_url;
    //private String message_send_url;
    private Integer agentid;
    private String corpid;
    private String corpsecret;

//    public String getAccessTokenUrl() {
//        return access_token_url;
//    }
//    public void setAccessTokenUrl(String access_token_url) {
//        this.access_token_url = access_token_url;
//    }
//
//    public String getMessageSendUrl() {
//        return message_send_url;
//    }
//    public void setMessageSendUrl(String message_send_url) {
//        this.message_send_url = message_send_url;
//    }

    public Integer getAgentid() {
        return agentid;
    }
    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public String getCorpid() {
        return corpid;
    }
    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }
    public void setCorpsecret(String corpsecret) {
        this.corpsecret = corpsecret;
    }


}
