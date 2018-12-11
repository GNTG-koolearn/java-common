package com.koolearn.guonei;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.guonei.exception.WXAccessTokenException;
import com.koolearn.guonei.exception.WXGetJsapiTicketException;
import com.koolearn.guonei.exception.WXPullUserInfoException;
import com.koolearn.guonei.model.AccessTokenRespVo;
import com.koolearn.guonei.model.SendTemplateMessageReqVo;
import com.koolearn.guonei.model.UserInfoRespVo;
import com.koolearn.guonei.utils.HttpUtil;

/**
 * Created by lilong01 on 2018/6/26.
 */
public class PublicClient {

    private String appId;
    private String secret;

    private static volatile PublicClient instance = null;

    private PublicClient(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    public static final PublicClient getInstance(String appId, String secret) {
        if (instance == null) {
            synchronized (PublicClient.class) {
                PublicClient client = new PublicClient(appId, secret);
                if (client == null) {
                    synchronized (PublicClient.class) {
                        client = new PublicClient(appId, secret);
                    }
                }
                instance = client;
            }
        }
        return instance;
    }

    public AccessTokenRespVo getAccessToken(String code) {
        String path = PublicConstants.GET_ACCESS_TOKEN_URL.replaceAll("APPID", appId)
                .replaceAll("SECRET", secret)
                .replaceAll("CODE", code);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            AccessTokenRespVo accessTokenRespVo = new AccessTokenRespVo();
            accessTokenRespVo.setAccess_token(re.getString("access_token"));
            accessTokenRespVo.setExpires_in(re.getIntValue("expires_in"));
            accessTokenRespVo.setOpenid(re.getString("openid"));
            accessTokenRespVo.setRefresh_token(re.getString("refresh_token"));
            accessTokenRespVo.setScope(re.getString("scope"));
            return accessTokenRespVo;
        } else {
            throw new WXAccessTokenException("获取access token失败");
        }
    }


    public UserInfoRespVo pullUserInfo(String accessToken, String openId) {
        String path = PublicConstants.PULL_USER_INFO_URL.replaceAll("ACCESS_TOKEN", accessToken)
                .replaceAll("OPENID", openId);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            UserInfoRespVo userInfoRespVo = new UserInfoRespVo();
            userInfoRespVo.setOpenid(re.getString("openid"));
            userInfoRespVo.setCity(re.getString("city"));
            userInfoRespVo.setCountry(re.getString("country"));
            userInfoRespVo.setHeadimgurl(re.getString("headimgurl"));
            userInfoRespVo.setNickname(re.getString("nickname"));
            userInfoRespVo.setProvince(re.getString("province"));
            userInfoRespVo.setUnionid(re.getString("unionid"));
            return userInfoRespVo;
        } else {
            throw new WXPullUserInfoException("拉去用户信息失败");
        }
    }


    public String getBaseAccessToken() {
        String path = PublicConstants.GET_BASE_ACCESS_TOKEN_URL.replaceAll("APPID", appId)
                .replaceAll("SECRET", secret);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            String accessToken = re.getString("access_token");
            return accessToken;
        } else {
            throw new WXAccessTokenException("获取access token失败");
        }
    }


    public String getJsapiTicket(String baseAccessToken) {
        String path = PublicConstants.GET_JSAPI_TICKET_URL.replaceAll("ACCESS_TOKEN", baseAccessToken);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            String ticket = re.getString("ticket");
            return ticket;
        } else {
            throw new WXGetJsapiTicketException("获取jsapi ticket失败");
        }
    }

    /**
     * 发送模板消息
     * @param accessToken
     * @param sendTemplateMessageReqVo
     * @return
     */
    public int sendTemplateMessage(String accessToken, SendTemplateMessageReqVo sendTemplateMessageReqVo) {
        String path = PublicConstants.SEND_TEMPLATE_MESSAGE_URL.replaceAll("ACCESS_TOKEN", accessToken);
        String json = JSONObject.toJSONString(sendTemplateMessageReqVo);
        JSONObject re = HttpUtil.httpsRequest(path, "POST", json);
        int recode = re.getIntValue("errcode");
        return recode;
    }

}
