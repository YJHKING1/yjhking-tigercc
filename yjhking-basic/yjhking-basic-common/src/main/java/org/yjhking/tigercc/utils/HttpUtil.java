package org.yjhking.tigercc.utils;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http工具类
 * <p>
 * <dependency>
 * <groupId>commons-codec</groupId>
 * <artifactId>commons-codec</artifactId>
 * <version>1.4</version>
 * </dependency>
 * <p>
 * <dependency>
 * <groupId>commons-httpclient</groupId>
 * <artifactId>commons-httpclient</artifactId>
 * <version>3.1</version>
 * </dependency>
 **/
public class HttpUtil {
    
    /**
     * 发送post请求
     * map.put("Uid" ,"xxx");
     * map.put("Key" ,"xxxxx");
     **/
    public static String sendPost(String url, Map<String, String> param) {
        //结果
        String result = null;
        try {
            //创建http客户端
            HttpClient client = new HttpClient();
            //创建Post请求
            PostMethod post = new PostMethod(url);
            //设置请求头
            //在头文件中设置转码
            post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //封装参数
            if (param != null && !param.isEmpty()) {
                Set<String> keySet = param.keySet();
                //取出所有的key
                Iterator<String> iterator = keySet.iterator();
                //用来封装参数的数组
                NameValuePair[] data = new NameValuePair[keySet.size()];
                int index = 0;
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = param.get(key);
                    data[index++] = new NameValuePair(key, value);
                }
                //把数据设置到请求体
                post.setRequestBody(data);
            }
            //执行发送
            client.executeMethod(post);
            //得到响应结果
            result = new String(post.getResponseBodyAsString().getBytes(StandardCharsets.UTF_8));
            post.releaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("网络请求失败[" + e.getMessage() + "]");
        }
        return result;
    }
    
    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }
    
    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }
    
    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }
    
    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        
        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();
        
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
    
    public static String httpGet(String url) {
        // 1 创建发起请求客户端
        try {
            HttpClient client = new HttpClient();
            // 2 创建要发起请求-tet
            GetMethod getMethod = new GetMethod(url);
            //            getMethod.addRequestHeader("Content-Type",
            //                    "application/x-www-form-urlencoded;charset=UTF-8");
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf8");
            // 3 通过客户端传入请求就可以发起请求,获取响应对象
            client.executeMethod(getMethod);
            // 4 提取响应json字符串返回
            return new String(getMethod.getResponseBodyAsString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
