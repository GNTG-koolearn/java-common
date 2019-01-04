package com.koolearn.guonei.textanalyzer;

import util.FileUtil;

import java.io.*;
import java.util.*;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/27
 */
public abstract class Segment {
    protected Set<String> keywords;//匹配词
    protected boolean isMatchKeyword;//是否进行匹配
    protected static Map<String,String> map = new HashMap();


    /**
     * 构造函数，初始化各个属性
     */
    public Segment(){
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isMatchKeyword = false;
        keywords=new HashSet<String>();
    }

    private void init() throws IOException {
        if(map.size() == 0){
            List<String> lemmasList = new ArrayList<String>();
            InputStream is = this .getClass().getResourceAsStream( "/lemmas.txt" ); // 可以正常使用
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String lineTxt;
            while((lineTxt = br.readLine()) != null){
                lemmasList.add(lineTxt);
            }
            br.close();
            is.close();
            for (String lemmas : lemmasList) {
                String[] arr = lemmas.split("\t");
                map.put(arr[0], null);
                if (arr.length > 1) {
                    map.put(arr[0], arr[1]);
                }
            }
        }
    }

    /**
     * 构造函数，初始化各个属性，初始化匹配词集
     * @param keywords 匹配词集
     */
    public Segment(Set keywords){
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isMatchKeyword = true;
        this.keywords = keywords;
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
        return keywords;
    }

    /**
     * @param keywrods the keywrods to set
     */
    public void setKeywords(Set<String> keywrods) {
        this.keywords = keywrods;
    }

    /**
     * 时态、单复数、英美拼写、变体等情况下还原单词形态
     * @param word
     * @return
     */
    public String restoreMorphology(String word){
        if(null != map.get(word)){
            return word;
        }else{
            for(String key : map.keySet()){
                if(null != map.get(key)){
                    for(String str : map.get(key).split(" ")){
                        if(word.equals(str)){
                            return key;
                        }
                    }
                }
            }
        }
        return word;
    }
}
