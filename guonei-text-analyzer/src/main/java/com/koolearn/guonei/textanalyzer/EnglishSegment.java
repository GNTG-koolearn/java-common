package com.koolearn.guonei.textanalyzer;


import com.alibaba.fastjson.JSON;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.util.*;
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
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    public EnglishSegment(String lemmasPath){
        super(lemmasPath);
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    public EnglishSegment(String lemmasPath, Set<String> keywrodSet){
        super(lemmasPath, keywrodSet);//设置关键集
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
                        if(keywrods.contains(word)){
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
        String content="Financial regulations in Britain have imposed a rather unusual rule on the bosses of big banks. Starting next year, any guaranteed bonus of top executives could be delayed 10 years if their banks are under investigation for wrongdoing. The main purpose of this “clawback” rule is to hold bankers accountable for harmful risk-taking and to restore public trust in financial institution. Yet officials also hope for a much larger benefit: more long term decision-making not only by banks but also by all corporations, to build a stronger economy for future generations.\n" +
                "“Short-termism” or the desire for quick profits, has worsened in publicly traded companies, says the Bank of England’s top economist. Andrew Haldane. He quotes a giant of classical economies, Alfred Marshall, in describing this financial impatience as acting like “Children who pick the plums out of their pudding to eat them at once” rather than putting them aside to be eaten last.\n" +
                "The average time for holding a stock in both the United States and Britain, he notes, has dropped from seven years to seven months in recent decades. Transient investors, who demand high quarterly profits from companies, can hinder a firm’s efforts to invest in long-term research or to build up customer loyalty. This has been dubbed “quarterly capitalism”.\n" +
                "In addition, new digital technologies have allowed more rapid trading of equities, quicker use of information, and thus shortens attention spans in financial markers. “There seems to be a predominance of short-term thinking at the expense of long-term investing,” said Commissioner Daniel Gallagher of the US Securities and Exchange Commission in speech this week.\n" +
                "In the US, the Sarbanes-Oxley Act of 2002 has pushed most public companies to defer performance bonuses for senior executives by about a year, slightly helping reduce “short-termism.” In its latest survey of CEO pay, The Wall Street Journal finds that “ a substantial part” of executive pay is now tied to performance.\n" +
                "Much more could be done to encourage “long-termism,” such as changes in the tax code and quicker disclosure of stock acquisitions. In France, shareholders who hold onto a company investment for at least two years can sometimes earn more voting rights in a company.\n" +
                "Within companies, the right compensation design can provide incentives for executives to think beyond their own time at the company and on behalf of all stakeholders. Britain’s new rule is a reminder to bankers that society has an interest in their performance, not just for the short term but for the long term.\n";
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
