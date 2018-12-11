package com.koolearn.guonei.model.miniprogram;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/02
 */
@Getter
@Setter
public class MPTemplateMsgReqVo implements Serializable {

    /**
     *  公众号appid，要求与小程序有绑定且同主体
     */
    private String appid;

    /**
     *  公众号模板id
     */
    private String template_id;

    /**
     *  小公众号模板消息所要跳转的url
     */
    private String url;

    /**
     *  公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
     */
    private Map miniprogram;

    /**
     *  公众号模板消息的数据
     */
    private Map data;
}
