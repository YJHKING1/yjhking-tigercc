package org.yjhking.tigercc;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.factory.Factory.Payment;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;

import java.util.UUID;

public class PayTest {
    public static void main(String[] args) throws Exception {
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用
            String orderNo = UUID.randomUUID().toString();
            AlipayTradePagePayResponse response =
                    Payment.Page().pay("我要花钱", orderNo, "100.00",
                            "http://course.tigercc.com:6002/pay.success.html");
            
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                
                System.out.println("调用成功");
                System.out.println(response.getBody());
            } else {
                System.err.println("调用失败，原因：" + response);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private static Config getOptions() {
        Config config = new Config();
        //协议
        config.protocol = "https";
        //网关
        config.gatewayHost = "openapi.alipaydev.com";
        //签名算法
        config.signType = "RSA2";
        //应用ID
        config.appId = sandboxSetting("ALIPAY_SANDBOX_APP_ID");
        // 应用私钥 ：加签
        config.merchantPrivateKey = sandboxSetting("ALIPAY_SANDBOX_PRIVATE_KEY");
        // 支付宝公钥 ：验签
        config.alipayPublicKey = sandboxSetting("ALIPAY_SANDBOX_PUBLIC_KEY");
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = System.getenv("ALIPAY_SANDBOX_NOTIFY_URL");
        return config;
    }

    private static String sandboxSetting(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(name + " is required for the Alipay sandbox example");
        }
        return value;
    }
}
