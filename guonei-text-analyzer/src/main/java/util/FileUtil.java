package util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is Description
 *
 * @author lilong01
 * @date 2018/12/28
 */
public class FileUtil {
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static List<String> readTxtFile(String filePath){
        List<String> retList = new ArrayList<String>();
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while((lineTxt = bufferedReader.readLine()) != null){
                    retList.add(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件，filePath = " + filePath);
            }

        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return retList;

    }

    public static void main(String argv[]){
        Map<String,String> map = new HashMap();
        String filePath = FileUtil.class.getClassLoader().getResource("lemmas.txt").getPath();
        List<String> list = readTxtFile(filePath);
        for(String lemmas : list){
            String[] arr = lemmas.split("\t");
            map.put(arr[0],null);
            if(arr.length > 1){
                map.put(arr[0],arr[1]);
            }
        }
        String word = "companies";
        if(null != map.get(word)){
            System.out.println(word);
        }else{
            for(String key : map.keySet()){
                if(null != map.get(key)){
                    for(String str : map.get(key).split(" ")){
                        if(word.equals(str)){
                            System.out.println(key);
                        }
                    }
                }
            }
        }

        //System.out.println(JSONObject.toJSONString(map));
    }
}
