package org.yjhking.tigercc.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 百度AI智能验证工具类
 *
 * @author YJH
 */
public class BaiduAiUtils {
    
    /**
     * 获取权限token
     *
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "ghe0Rr1qMySCKGXHoQr6idT9";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "wX0OTMcM9lHRUwA5FV1rsoPefRKaBHVK";
        return getAuth(clientId, clientSecret);
    }
    
    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Secret Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            // 返回结果示例
            System.err.println("result:" + result);
            //将json格式的字符串 - 转成json格式的对象
            JSONObject jsonObject = JSONObject.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
    
    public static Boolean textCensor(String text) {
        System.out.println("要审核的文字");
        System.out.println(text);
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            String param = "text=" + text;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String conclusion = jsonObject.getString("conclusion");
            if ("合规".equals(conclusion)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Boolean imgCensor(String imgUrl) {
        System.out.println("要审核的图片地址");
        System.out.println(imgUrl);
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            // 本地文件路径
            String imgParam = URLEncoder.encode(imgUrl, "UTF-8");
            String param = "imgUrl=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String conclusion = jsonObject.getString("conclusion");
            if ("合规".equals(conclusion)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
