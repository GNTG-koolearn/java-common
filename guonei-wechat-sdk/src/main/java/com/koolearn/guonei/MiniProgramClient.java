package com.koolearn.guonei;


import com.alibaba.fastjson.JSONObject;
import com.koolearn.guonei.exception.miniprogram.AnalysisException;
import com.koolearn.guonei.exception.miniprogram.Code2SessionException;
import com.koolearn.guonei.model.miniprogram.AccessTokenRespVo;
import com.koolearn.guonei.model.miniprogram.Code2SessionRespVo;
import com.koolearn.guonei.model.miniprogram.SendUniformMessageReqVo;
import com.koolearn.guonei.model.miniprogram.analysis.AnalysisDailyRetailRespVo;
import com.koolearn.guonei.model.miniprogram.analysis.AnalysisDailySummaryRespVo;
import com.koolearn.guonei.utils.HttpUtil;
import com.koolearn.guonei.utils.WechatDecriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lilong01 on 2018/6/26.
 */
public class MiniProgramClient {

    Logger log = LoggerFactory.getLogger(MiniProgramClient.class);

    private String appId;
    private String secret;

    public Code2SessionRespVo code2Session(String code, String encryptedData, String iv){
        String path = MiniProgramConstants.CODE2SESSION_URL.replaceAll("APPID", appId)
                .replaceAll("SECRET", secret)
                .replaceAll("JSCODE", code);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            Code2SessionRespVo code2SessionRespVo = new Code2SessionRespVo();
            code2SessionRespVo.setOpenId(re.getString("openid"));
            code2SessionRespVo.setSessionKey(re.getString("session_key"));
            //解析微信加密的用户敏感信息
            JSONObject decryptedData = WechatDecriptUtil.getDecryptedData(re.getString("session_key"), encryptedData, iv);
            code2SessionRespVo.setUnionId(decryptedData.getString("unionId"));
            code2SessionRespVo.setAvatarUrl(decryptedData.getString("avatarUrl"));
            code2SessionRespVo.setCity(decryptedData.getString("city"));
            code2SessionRespVo.setCountry(decryptedData.getString("country"));
            code2SessionRespVo.setGender(decryptedData.getString("gender"));
            code2SessionRespVo.setLanguage(decryptedData.getString("language"));
            code2SessionRespVo.setNickName(decryptedData.getString("nickName"));
            code2SessionRespVo.setProvince(decryptedData.getString("province"));
            code2SessionRespVo.setWatermark(decryptedData.getString("watermark"));
            return code2SessionRespVo;
        }else{
            throw new Code2SessionException("code2session failed，recode = " + recode);
        }
    }


    public AccessTokenRespVo getAccessToken(){
        String path = MiniProgramConstants.GET_ACCESS_TOKEN_URL.replaceAll("APPID", appId)
                .replaceAll("APPSECRET", secret);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        if (0 == recode) {
            AccessTokenRespVo accessTokenRespVo = new AccessTokenRespVo();
            accessTokenRespVo.setAccessToken(re.getString("access_token"));
            accessTokenRespVo.setExpires(re.getInteger("expires_in"));
            return accessTokenRespVo;
        }else{
            throw new Code2SessionException("getAccessToken failed, recode = " + recode);
        }
    }


    public int sendUniformMessage(String accessToken, SendUniformMessageReqVo sendUniformMessageReqVo){
        String path = MiniProgramConstants.SEND_UNIFORM_MESSAGE_URL.replaceAll("ACCESS_TOKEN", accessToken);
        String json = JSONObject.toJSONString(sendUniformMessageReqVo);
        log.info("invoking the send uniform message interface, the param is : " + json);
        JSONObject re = HttpUtil.httpsRequest(path, "GET", "");
        int recode = re.getIntValue("errcode");
        return recode;
    }

    /**
     * 获取用户访问小程序日留存
     *
     * @param accessToken 接口调用凭证
     * @param beginDate   开始日期 格式为yyyyMMdd
     * @param endDate     结束日期 限定查询1天数据 格式为yyyyMMdd
     * @return
     */
    public AnalysisDailyRetailRespVo getAnalysisDailyRetain(String accessToken, String beginDate, String endDate) throws AnalysisException {
        String path = MiniProgramConstants.GET_ANALYSIS_DAILY_RETAIN_URL.replace("ACCESS_TOKEN", accessToken);
        Map<String, String> params = new HashMap<String, String>();
        params.put("begin_date", beginDate);
        params.put("end_date", endDate);
        JSONObject jsonObject = HttpUtil.httpsRequest(path, "POST", JSONObject.toJSONString(params));
        if (jsonObject.getInteger("errcode") == null) {
            AnalysisDailyRetailRespVo analysisDailyRetailRespVo = JSONObject.parseObject(jsonObject.toJSONString(), AnalysisDailyRetailRespVo.class);
            return analysisDailyRetailRespVo;
        } else {
            throw new AnalysisException("errmsg = " + jsonObject.getString("errmsg") + "=====errcode = " + jsonObject.getInteger("errcode"));
        }

    }

    /**
     * 获取用户访问小程序数据概况
     *
     * @param accessToken 接口调用凭证
     * @param beginDate   开始日期 格式为yyyyMMdd
     * @param endDate     结束日期 限定查询1天数据 格式为yyyyMMdd
     * @return
     * @throws AnalysisException
     */
    public List<AnalysisDailySummaryRespVo> getAnalysisDailySummary(String accessToken, String beginDate, String endDate) throws AnalysisException {
        String path = MiniProgramConstants.GET_ANALYSIS_DAILY_SUMMARY_URL.replace("ACCESS_TOKEN", accessToken);
        Map<String, String> params = new HashMap<String, String>();
        params.put("begin_date", beginDate);
        params.put("end_date", endDate);
        JSONObject jsonObject = HttpUtil.httpsRequest(path, "POST", JSONObject.toJSONString(params));
        if (jsonObject.getInteger("errcode") == null) {
            List<AnalysisDailySummaryRespVo> analysisDailySummaryRespVos = JSONObject.parseArray(jsonObject.getString("list"), AnalysisDailySummaryRespVo.class);
            return analysisDailySummaryRespVos;
        } else {
            throw new AnalysisException("errmsg = " + jsonObject.getString("errmsg") + "=====errcode = " + jsonObject.getInteger("errcode"));
        }
    }

    /**
     * 获取小程序二维码 适用于需要的码数量较少的业务场景 通过该接口生成的小程序码 永久有效 有数量限制
     * 如果调用成功，会直接返回图片二进制内容，如果请求失败，会返回 JSON 格式的数据。
     *
     * @param accessToken 接口调用凭证
     * @param url         扫码进入的小程序页面路径，最大长度128字节，不能为空
     * @param width       二维码的宽度，单位px，最小280，最大1280  默认值为430
     * @return
     * @throws RuntimeException
     */
    public InputStream createWXAQRCode(String accessToken, String url, Integer width) throws RuntimeException {
        String path = MiniProgramConstants.CREATE_WXA_QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        Map map = new HashMap();
        map.put("path", url);
        if (width == null) {
            width = 430;
        }
        map.put("width", width);
        Map responseMap = HttpUtil.httpsRequestMap(path, "POST", JSONObject.toJSONString(map));
        if (responseMap.get("errcode") == null) {
            InputStream inputStream = (InputStream) responseMap.get("inputstream");
            return inputStream;
        } else {
            throw new RuntimeException("获取小程序二维码出错，errmsg = " + responseMap.get("errmsg"));
        }
    }
}
