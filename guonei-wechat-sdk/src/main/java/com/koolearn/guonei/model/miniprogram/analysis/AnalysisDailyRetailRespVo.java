package com.koolearn.guonei.model.miniprogram.analysis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 用户访问小程序日留存响应对象
 *
 * @author xiehua
 * @date 2018/12/04
 */
@Setter
@Getter
public class AnalysisDailyRetailRespVo implements Serializable {
    /**
     * 日期
     */
    private String refDate;

    List<VisitUvRespVo> visitUvNew;

    List<VisitUvRespVo> visitUv;
}
