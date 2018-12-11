package com.koolearn.guonei.model.miniprogram;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lilong01 on 2018/6/26.
 */
@Getter
@Setter
public class AccessTokenRespVo implements Serializable {
    private String accessToken;
    private int expires;

}
