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
        config.appId = "2021000121654430";
        // 应用私钥 ：加签
        config.merchantPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMFtS2mrDIHaDIZb8cGZIp5jZbIVd3Cg5iLdlXumb7+xv/m7qoTOarVDlCvaa1KRhY3cmRVOZrmsBXZ/iOy0BeGPWttShsB/JXvOmoa2WhAO1UlXdBdlPgUYOOn/y8gEkB+4L0OK4vEiK4pUGwg/j7sKMCGLCDOu6hXq+2UnO9V0o4spp5vaWyAxwmqG9Ai3J1pmHQvSIuLhi/3VFFoWVUYTO0/aO+23H/9HKyTQRpeh9hnZvzUqtBIiI9UCOUCxv0sKAEQgfPAQVMrhrM/WxwC1bp6EhKtCSI1mIuk2Dh0yHnm7TL+wBXSJjzq1QvEH2ycY7dUWkkH08GzWTobIWpAgMBAAECggEAcyEAR+Hxj0TqMkeMPCe3u796xIIuQ6J8F6vehv6mTEgexn7RdCdfG0bomDUMoK3ZWI9NbKb9h0ly8WJ9H71idPLGVbAgPEh7niefT86TgzIj2fPMQnxgimXzXG+XYcN8PKjbAqLiY8TfzCfkSJDtOsv762knB3KTUwNiHNC+VcgCJKAP6ggqr/MkeTn51ahVyySnZtpq3jr2k63C4FF5gERDIadHcY/P6zTGG4+8U1WP6XjA+vkv/69KRbZdCePO670z6CjYK5LJ3dRqF45mYEpuSYpY8tgJlNf6tI0770VrrW0SNq39Ptqa5wax3OvUNR62hJOjrk+sewulOroA0QKBgQDSyKMsE6WHwrWmmKvkbyCBiBWRof3MiQ1BJtaXD3N1aIt/coHNUJsaZB7iBw0Iz8w8oDaYaPYWVRLZVNwHg/fE0CRoYxwdDK/74hTxBuAJaDwXRtU9W7qFbCcRtesi/Zc5JUOCg2pPz/jYLU+R1ZPJ+nYgsBa34FPEzQk7fuW+9wKBgQCqI/GZNFa4irgT8af2EWI3lHtMWp55I0jvaFWT/LQnseVkYU9mmLpuj6nBL8aNVWqRI/VxJb0OBqlGViuVdG/tHRNIM6JLKw9ckrQMADbCTqBaI+X9qEOKBfUvv6sSgNAAKzGL1RVz1mJbkJprWe90YmTkoFtzB0g6YVljPHiYXwKBgQC2FPXHrI0QkqVibX4HjeuRmaGwNTRODJIzlr5GreU6Jf5jMOJqHthtWtHGxPqAgyjVsjyXOgxizEjFDuYnY9uT242n0v3FJmGbf/hBIYRE/BL0tI6eO3ALs5qoVPLzPgb1KXiA9M7QdUJsU+/pdYr+Lmr+374wRwt19PyltX/3jwKBgCoJN2fixopRu6kxy43APcP9q3jx6rWDQkHkRUCK7vffvA1Q/Y0brvQiCo7wft3sfKffeoS6Djuo6am3II2MdxreVCVCnJZ1zRXSKSnN47meM4AR3oENUaKIVku1z06DBOEFyvgBbYtxLFvzHjPnxIG8zbCu7ek9o1rJMoySJDmBAoGAIXZwV7vloNzS1atib2ZEsP9i71oeWF/GY1ZkReCMKlp/eZFk43BlSfgHlZ26BA1OEk2f6cHEzd25lmzZXQOOyAwSjpohQ84L+Hppb+dfzsByPWuDYYeY4giIgUM/sadOeJJgplmg8JDow/bRJEP527tfg6Y6T74fslCeNBmpFNc=";
        // 支付宝公钥 ：验签
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg0EO/WCBl+tt//yHN04+pzwoeIdtEt2LK/H37Agrttn3CPjYpebOPb3EkppuAfw0yUiFzRjEGvy8dA5jlvHY/v0lEenJv3wVhpq+lm/jcza6zYvq2eNQB1+IBPIabRe6bGeXP34OSY2ITkDb5eoPxW26lKzOssthdyXFAoP8ScknoFVdZPbH128KDOLZ8P4eIVg7CRTcz4XzdULCXxaXuHQL/JluEfAJENq0Y5RYculJxBRgfavH/zLLFwNnkOG4KuUTqXe3D1P1sUnnV4brhl4hdy3jiSk36FXVi1Cl3k286XQgxgtIPs8Uo7zY5OnmcS1P9qwC5w6vvIzqmvImwQIDAQAB";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "http://localhost:10010/tigercc/pay/pay/notify";
        return config;
    }
}