package com.koolearn.guonei;


import com.alibaba.fastjson.JSONObject;
import com.koolearn.guonei.model.miniprogram.AccessTokenRespVo;
import com.koolearn.guonei.model.miniprogram.analysis.AnalysisDailyRetailRespVo;
import com.koolearn.guonei.model.miniprogram.analysis.AnalysisDailySummaryRespVo;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class MiniProgramClientTest {

    private MiniProgramClient client;

    @Before
    public void init(){
        //client = MiniProgramClient.getInstance("wx856d4c336530c045","d4d9ba47b047dd793effd98f76a49e0f");
    }

    @Test
    public void code2Session() {
    }

    @Test
    public void getAccessToken() {
    }

    @Test
    public void sendUniformMessage() {
        AccessTokenRespVo accessTokenRespVo = client.getAccessToken();
        System.out.println(accessTokenRespVo.getAccessToken());
    }

    @Test
    public void testGetAnalysisDailyRetain() {
        String accessToken = "16_9CSEClfYXTbEtrveXdsuskOL09ETxa3um7rEKDrBPR9vQqo71TA02SwgfcCEQoUKU3iO1mQ2XgtFGJzPwb3G_KtJZhhKZk-meKeMZ3VHhBgLla23yyxjzMPfEnC-XC6QuAhDBI-lAmh_iO9_MAKjAGAUIS";
        AnalysisDailyRetailRespVo analysisDailyRetailRespVo = client.getAnalysisDailyRetain(accessToken, "20181204", "20181204");
        System.out.println(JSONObject.toJSONString(analysisDailyRetailRespVo));
    }

    @Test
    public void testGetAnalysisDailySummary() {
        String accessToken = "16_9CSEClfYXTbEtrveXdsuskOL09ETxa3um7rEKDrBPR9vQqo71TA02SwgfcCEQoUKU3iO1mQ2XgtFGJzPwb3G_KtJZhhKZk-meKeMZ3VHhBgLla23yyxjzMPfEnC-XC6QuAhDBI-lAmh_iO9_MAKjAGAUIS";
        List<AnalysisDailySummaryRespVo> analysisDailySummaryRespVos = client.getAnalysisDailySummary(accessToken, "20181204", "20181204");
        System.out.println(JSONObject.toJSONString(analysisDailySummaryRespVos));
    }

    @Test
    public void testCreateWXAQRCode() {
        String accessToken = "16_9CSEClfYXTbEtrveXdsuskOL09ETxa3um7rEKDrBPR9vQqo71TA02SwgfcCEQoUKU3iO1mQ2XgtFGJzPwb3G_KtJZhhKZk-meKeMZ3VHhBgLla23yyxjzMPfEnC-XC6QuAhDBI-lAmh_iO9_MAKjAGAUIS";
        try {
            InputStream response = client.createWXAQRCode(accessToken, "https://www.baidu.com", 430);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}