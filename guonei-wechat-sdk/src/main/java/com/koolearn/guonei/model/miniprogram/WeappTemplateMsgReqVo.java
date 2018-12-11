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
public class WeappTemplateMsgReqVo implements Serializable {

    /**
     *  小程序模板ID
     */
    private String template_id;

    /**
     *  小程序页面路径
     */
    private String page;

    /**
     *  小程序模板消息formid
     */
    private String form_id;

    /**
     *  小程序模板数据
     */
    private Map data;

    /**
     *  小程序模板放大关键词
     */
    private String emphasis_keyword;
}
