package com.koolearn.guonei.model.miniprogram;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/02
 */
@Getter
@Setter
public class SendUniformMessageReqVo implements Serializable {
    private String touser;
    private WeappTemplateMsgReqVo weapp_template_msg;
    private MPTemplateMsgReqVo mp_template_msg;

}
