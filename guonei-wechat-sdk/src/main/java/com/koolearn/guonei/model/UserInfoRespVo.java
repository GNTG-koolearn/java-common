package com.koolearn.guonei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lilong01 on 2018/6/26.
 */
@Getter
@Setter
public class UserInfoRespVo implements Serializable {
    private String openid;
    private String nickname;
    private short sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String unionid;

}
