package com.koolearn.guonei.model.miniprogram.analysis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户访问小程序数据概况响应对象
 *
 * @author xiehua
 * @date 2018/12/04
 */
@Setter
@Getter
public class AnalysisDailySummaryRespVo implements Serializable {
    /**
     * 日期，格式为yyyymmdd
     */
    private String refDate;

    /**
     * 累计用户数
     */
    private Integer visitTotal;

    /**
     * 转发次数
     */
    private Integer sharePv;

    /**
     * 转发人数
     */
    private Integer shareUv;
}
