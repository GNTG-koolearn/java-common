package com.koolearn.guonei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lilong01 on 2018/6/26.
 */
@Getter
@Setter
public class AccessTokenRespVo implements Serializable {
    private String openid;
    private String refresh_token;
    private int expires_in;
    private String access_token;
    private String scope;

}
