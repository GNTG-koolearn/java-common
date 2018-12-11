package com.koolearn.guonei.model.miniprogram.analysis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This is Description
 *
 * @author xiehua
 * @date 2018/12/04
 */
@Setter
@Getter
public class VisitUvRespVo implements Serializable {
    /**
     * 标识，0开始，表示当天，1表示一天后，依此类推，取值分别为：0，1，2，3，4，5，6，7，14，30
     */
    private Integer key;

    /**
     * key对应日期的新增用户数/活跃用户数（key=0）时或留存用户数（k>0时）
     */
    private Integer value;
}
