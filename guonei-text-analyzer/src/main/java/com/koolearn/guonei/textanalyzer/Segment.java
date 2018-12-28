package com.koolearn.guonei.textanalyzer;

import java.util.HashSet;
import java.util.Set;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/27
 */
public abstract class Segment {
    protected Set<String> keywrods;//匹配词
    protected boolean isMatchKeyword;

    /**
     * 构造函数，初始化各个属性
     */
    public Segment(){
        isMatchKeyword = false;
        keywrods=new HashSet<String>();
    }

    /**
     * 构造函数，初始化各个属性，初始化匹配词集
     * @param keywrods 匹配词集
     */
    public Segment(Set keywrods){
        isMatchKeyword = true;
        this.keywrods = keywrods;
    }

    /**
     * 对字符串内容进行分词
     * @param content 内容
     * @return 由空格符作为分隔符的分词结果String
     */
    public abstract String segment(String content);

    /**
     * @return the keywrods
     */
    public Set<String> getKeywrods() {
        return keywrods;
    }

    /**
     * @param keywrods the keywrods to set
     */
    public void setKeywords(Set<String> keywrods) {
        this.keywrods = keywrods;
    }
}
