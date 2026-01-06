package com.attendance.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.attendance.framework.config.WxWorkConfig;
import com.attendance.project.business.service.IUserWxworkService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import com.attendance.project.business.domain.*;
import com.google.gson.Gson;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PushWxWorkMes {//extends BaseController {

    @Autowired
    private WxWorkConfig wxWorkConfig;

    @Autowired
    private IUserWxworkService wxWorkUserService;

    // 缓存access_token，避免频繁获取
    private static String accessToken;
    private static long expiredTime;

    //public class WechatUtil{
    public static Map<String,Object> tokenMap = new HashMap<>();

    public static final String ACCESS_TOKEN_URL="https://qyapi.weixin.qq.com/cgi-bin/gettoken?";
    public static final String MESSAGE_SEND_URL="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    public static final String GETUSERINFO="https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?";


    // 获取token
    private void getAccessToken() throws IOException {
        if (accessToken != null && System.currentTimeMillis() < expiredTime) {
            return;
        }
        if (StringUtils.isEmpty(wxWorkConfig.getCorpid()) || StringUtils.isEmpty(wxWorkConfig.getCorpsecret()) ) {
            throw new IOException("缺少corpId或者corpSecret!");
        }
        String url =  ACCESS_TOKEN_URL+"corpid=" + wxWorkConfig.getCorpid().trim() + "&corpsecret=" + wxWorkConfig.getCorpsecret().trim();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        if(jsonObject.getIntValue("errcode") == 0) {
            accessToken = jsonObject.getString("access_token");
            // access_token有效期为7200秒，这里设置提前60秒过期
            expiredTime = System.currentTimeMillis() + (7200 - 60) * 1000;
        } else {
            //log.error(jsonObject.getString("errmsg"));
            //throw new IOException(ErrorCodeText.errorMsg(jsonObject.getIntValue("errcode")));
        }
    }

        //消息请求体-纯文本
        public  String createTextData(String touser,String content) {
            Gson gson = new Gson();
            TextMessage tm = new TextMessage();
            Text t = new Text();
            t.setContent(content);
            tm.setMsgtype("text");
            tm.setAgentid(wxWorkConfig.getAgentid());
            tm.setText(t);
            tm.setTouser(touser);

            return gson.toJson(tm);

        }

        /**
        消息请求体-文字卡片
         * @param tousers 企业微信号--成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）
         * @param title 标题，不超过128个字符
         * @param description 描述，不超过512个字符
         * @param url 点击后跳转的链接
        */
   public  String createTextcardData(String tousers, String title, String description, String url) {
       if (tousers != "") {
           Gson gson = new Gson();
           TextcardMessage tcm = new TextcardMessage();
           Textcard tc = new Textcard();
           tc.setTitle(title);
           tc.setDescription(description);
           tc.setUrl(url);
           tcm.setAgentid(wxWorkConfig.getAgentid());
           tcm.setMsgtype("textcard");
           tcm.setTouser(tousers);
           tcm.setTextcard(tc);

           return gson.toJson(tcm);
       } else {
           return "";
       }
   }

    /**
     * 获取企业微信用户id
     * @param code
     * @return
     * @throws IOException
     */
    public  String getUserInfoByCode(String code) throws IOException {
        getAccessToken();
//        if (StringUtils.isEmpty(qywxConfig.getGetUserInByCodeUrl())){
//            throw new IOException("缺少企业微信获取用户url!");
//        }
        // 发送消息
        String url =GETUSERINFO + "access_token=" + accessToken + "&code=" + code;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

        int errcode = jsonObject.getIntValue("errcode");
        if (errcode != 0) {
            String errMsg = jsonObject.getString("errmsg");
            System.err.println(errMsg);
            //throw new IOException(ErrorCodeText.errorMsg(errcode));
        }

        System.out.println("获取访问用户身份接口返回信息：" + jsonObject);
        String userId = jsonObject.getString("userid");
        System.out.println(userId);
        return userId; // 返回用户ID
    }

    //根据企业微信获取用户系统账号
    public  String getLoginNameByTouser(String touser){

        String result = "";
        UserWxwork param = new UserWxwork();
        param.setTouser(touser);
        try {
            List<UserWxwork> userInfos = wxWorkUserService.selectWxWorkUserList(param);
            if (userInfos.size() > 0){
                return userInfos.get(0).getLoginName();
            }
        }catch(Exception ex)
        {
            result=ex.getMessage();

        }
        return result;
    }

        //根据系统账号获取企业微信用户
    public  String getTouserByLoginName(String loginName){

       String touser="";
       try {
           UserWxwork userInfo= wxWorkUserService.selectWxWorkUserByLoginName(loginName);
           if(userInfo!=null)
           {
               touser=userInfo.getTouser();
           }
       }catch(Exception ex)
       {
           String strmes=ex.getMessage();

       }
       return touser;
   }


    /**
     * 发送消息
     * @param loginNames 接收推送的本系统用户账号
     * @param title  推送标题，不超过128个字符
     * @param description 描述，不超过512个字符
     * @param url 点击后跳转的链接
     * @throws IOException
     */
        public  void sendMessage(List<String> loginNames, String title, String description, String url) throws IOException {
            StringBuilder touserb=new StringBuilder();//系统账号转企业微信账号
            String tousers="";
            if(loginNames.size()>=1) {
                for (String loginName : loginNames) {
                    String touser = getTouserByLoginName(loginName);
                    touserb.append(touser);
                    touserb.append("|");
                }
                touserb.deleteCharAt(touserb.length() - 1);
                tousers=touserb.toString();
            }

            String content=createTextcardData(tousers, title,description,url);//填充文字卡片

            getAccessToken();//获取accesstoken

            // 发送消息
            String access_url =MESSAGE_SEND_URL + accessToken;
            HttpPost httpPost = new HttpPost(access_url);
            httpPost.setEntity(new StringEntity(content, "utf-8"));
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            if (jsonObject.getIntValue("errcode") != 0){
                System.out.println(jsonObject.getString("errmsg"));
                //throw new IOException(ErrorCodeText.errorMsg(jsonObject.getIntValue("errcode")));
            }

        }

}
