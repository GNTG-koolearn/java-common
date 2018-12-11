package com.koolearn.guonei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 模板消息对象
 *
 * @author xiehua
 * @date 2018/12/04
 */
@Setter
@Getter
public class TemplateMessage implements Serializable {
    /**
     * 接收者（用户）的 openid
     */
    private String toUser;

    /**
     * 所需下发的模板消息的id
     */
    private String templateId;

    private String keyword1;

    private String keyword2;
}
