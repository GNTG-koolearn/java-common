package com.koolearn.guonei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by lilong01 on 2018/6/26.
 */
@Getter
@Setter
public class SendTemplateMessageReqVo implements Serializable {
    private String touser;
    private String template_id;
    private String url;
    private Map miniprogram;
    private Map data;

}
