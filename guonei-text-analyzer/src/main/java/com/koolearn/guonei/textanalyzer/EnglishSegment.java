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
        super();
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    public EnglishSegment(Set<String> keywrodSet){
        super(keywrodSet);//设置停用词
        analyzer=new StandardAnalyzer();//默认标准分词器
    }

    /**
     * 英文分词
     * step1 英文词法分析，去除数字、连字符、标点符号、特殊字符
     * step2 去停用词
     * step3 词干提取
     * @param content 文本内容
     * @return
     */
    public String segment(String content){
        StringBuilder sb=new StringBuilder();
        try {
            TokenStream tokenStream = analyzer.tokenStream(null, content);
            //设置波特词干提取器，自动去除停用词
            tokenStream=new PorterStemFilter(tokenStream);
            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            CharTermAttribute term=tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();//重置
            while( tokenStream.incrementToken() ){
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                //去除数字
                String word=term.toString().replaceAll("[^a-zA-Z]", "");
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
        String content="This year marks exactly two countries since the publication of Frankenstein; or, The Modern Prometheus, by Mary Shelley. Even before the invention of the electric light bulb, the author produced a remarkable work of speculative fiction that would foreshadow many ethical questions to be raised by technologies yet to come.\n" +
                " Today the rapid growth of artificial intelligence (AI) raises fundamental questions: “What is intelligence, identify, or consciousness? What makes humans humans?”\n" +
                "What is being called artificial general intelligence, machines that would imitate the way humans think, continues to evade scientists. Yet humans remain fascinated by the idea of robots that would look, move, and respond like humans, similar to those recently depicted on popular sci-fi TV series such as “Westworld” and “Humans”.\n" +
                "Just how people think is still far too complex to be understood, let alone reproduced, says David Eagleman, a Stanford University neuroscientist. “We are just in a situation where there are no good theories explaining what consciousness actually is and how you could ever build a machine to get there.”\n" +
                "But that doesn’t mean crucial ethical issues involving AI aren’t at hand. The coming use of autonomous vehicles, for example, poses thorny ethical questions. Human drivers sometimes must make split-second decisions. Their reactions may be a complex combination of instant reflexes, input from past driving experiences, and what their eyes and ears tell them in that moment. AI “vision” today is not nearly as sophisticated as that of humans. And to anticipate every imaginable driving situation is a difficult programming problem.\n" +
                "Whenever decisions are based on masses of data, “you quickly get into a lot of ethical questions,” notes Tan Kiat How, chief executive of a Singapore-based agency that is helping the government develop a voluntary code for the ethical use of AI. Along with Singapore, other governments and mega-corporations are beginning to establish their own guidelines. Britain is setting up a data ethics center. India released its AI ethics strategy this spring.\n" +
                "On June 7 Google pledged not to “design or deploy AI” that would cause “overall harm,” or to develop AI-directed weapons or use AI for surveillance that would violate international norms. It also pledged not to deploy AI whose use would violate international laws or human rights.\n" +
                "While the statement is vague, it represents one starting point. So does the idea that decisions made by AI systems should be explainable, transparent, and fair.\n" +
                "To put it another way: How can we make sure that the thinking of intelligent machines reflects humanity’s highest values? Only then will they be useful servants and not Frankenstein’s out-of-control monster.\n";
        Set<String> keywords = new HashSet<String>();
        keywords.add("intellig");
        keywords.add("robot");
        EnglishSegment es=new EnglishSegment(keywords);
        String result=es.segment(content);
        Map map = es.getTF(result);
        System.out.println(JSON.toJSONString(map));
    }

}
