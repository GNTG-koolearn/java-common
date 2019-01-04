package com.koolearn.guonei.textanalyzer;


import com.alibaba.fastjson.JSON;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/27
 */
public class EnglishSegment extends Segment{

    private Analyzer analyzer;//英文分词器


    public EnglishSegment(){
        super();
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    public EnglishSegment(Set<String> keywrodSet){
        super(keywrodSet);//设置关键集
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    /**
     * 英文分词
     * step1 英文词法分析，去除数字、连字符、标点符号、特殊字符
     * step2 匹配关键词
     * step3 还原单词形态
     * @param content 文本内容
     * @return
     */
    public String segment(String content){
        StringBuilder sb=new StringBuilder();
        try {
            TokenStream tokenStream = analyzer.tokenStream(null, content);
            CharTermAttribute term=tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();//重置
            while( tokenStream.incrementToken() ){
                //去除数字
                String word = term.toString().replaceAll("[^a-zA-Z]", "");
                //形态还原根据 Version 6 of the 12dicts word lists 提供的 2+2+3 lem list (http://wordlist.aspell.net/12dicts-readme/#223lem) 来转换
                word = restoreMorphology(word);
                if(word.length() > 0){
                    if(isMatchKeyword){
                        if(keywords.contains(word)){
                            sb.append(word+ " ");
                        }
                    }else{
                        sb.append(word+ " ");
                    }
                }
            }
            tokenStream.end();
            tokenStream.close();
        } catch (IOException ex) {
            Logger.getLogger(EnglishSegment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    /**
     * 计算TF
     * @param content 文本内容
     * @return
     */
    public Map<String,Integer> getTF(String content){
        String[] words=content.split(" ");
        Map<String,Integer> tf=new HashMap<String,Integer>();
        for(String word: words){
            if(tf.containsKey(word)){
                tf.put(word, tf.get(word)+1);
            }else{
                tf.put(word, 1);
            }
        }
        return tf;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public static void main(String args[]){
        String content="catalogue";
        Set<String> keywords = new HashSet<String>();
        keywords.add("intellig");
        keywords.add("robot");
        //String lemmasPath = EnglishSegment.class.getClassLoader().getResource("lemmas.txt").getPath();
        EnglishSegment es=new EnglishSegment();
        String result=es.segment(content);
        Map map = es.getTF(result);
        System.out.println(JSON.toJSONString(map));
    }

}
