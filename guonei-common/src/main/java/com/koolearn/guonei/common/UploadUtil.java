package com.koolearn.guonei.common;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: chenzhongyong
 * Date: 2019/1/14
 * Time: 10:48
 */
public class UploadUtil {

    /**
     * 上传文件到cdn
     *
     * @param paramMap 参数map
     *                 type: 文件类型字符串："picture"，"video"，"doc"（就这三种）
     *                 base: 项目名用于区分上传路径：比如小教室club，生成“/club/picture/”路径
     *                 fileName: 文件名
     *                 project：一般读取配置cdn_project
     *                 appkey：一般读取配置cdn_appkey
     *                 serverUrl：一般读取配置cdn_upload_url
     *                 inputStream：上传文件的文件流
     *
     *
     * 例子：
     *         Map<String, Object> params = new HashMap<>();
     *         params.put("type", "picture");
     *         params.put("base",“club”);
     *         params.put("fileName",fileName);
     *         params.put("project", getProperty("cdn_project"));
     *         params.put("appkey", getProperty("cdn_appkey"));
     *         params.put("serverUrl",getProperty("cdn_upload_url"));
     *         params.put("inputStream", fin);
     * @return
     * @throws IOException
     */
    public static Map upload2CDN(Map<String, Object> paramMap) throws IOException {
        String fileName = (String)paramMap.get("fileName");
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = String.format("%s.%s", uuid, extName);
        String base = (String)paramMap.get("base");
        String type = (String) paramMap.get("type");
        String filePath = String.format("/%s/%s/",base,type);
        InputStream fin = (InputStream) paramMap.get("inputStream");


        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("filePath",filePath);
        params.put("fileName",fileName);
        params.put("project", paramMap.get("project"));
        params.put("appkey", paramMap.get("appkey"));
        params.put("serverUrl",paramMap.get("serverUrl"));
        String responseJson = upload(fin, params);
        return JSON.parseObject(responseJson, Map.class);
    }

    private static String upload(InputStream inputStream, Map<String, Object> params) {
        final String serverUrl = params.get("serverUrl").toString();// 第三方服务器请求地址
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(serverUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, params.get("fileName").toString());// 文件流
            builder.addTextBody("type", params.get("type").toString());
            builder.addTextBody("project", params.get("project").toString());
            builder.addTextBody("appkey", params.get("appkey").toString());
            builder.addTextBody("cusdir",params.get("filePath").toString());
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
