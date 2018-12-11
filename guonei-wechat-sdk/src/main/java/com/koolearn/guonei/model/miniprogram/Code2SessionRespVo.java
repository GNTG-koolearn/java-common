package com.koolearn.guonei.model.miniprogram;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lilong01 on 2018/6/26.
 */
@Getter
@Setter
public class Code2SessionRespVo implements Serializable {
    private String openId;
    private String unionId;
    private String country;
    private String watermark;
    private String gender;
    private String province;
    private String city;
    private String avatarUrl;
    private String nickName;
    private String language;
    private String sessionKey;

}
