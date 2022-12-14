package org.yjhking.tigercc.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 图片验证码发送相关配置
 *
 * @author YJH
 */
@Data
@Component
//自动从配置读取，按照前缀过滤出配置项，同名匹配给对象的属性
@ConfigurationProperties(prefix = "tigercc.imgverifycode")
@RefreshScope
public class IMGVerifyCodeProperties {
    /**
     * 验证码长度
     */
    private int length;
    /**
     * 过期时间
     */
    private int expire;
}
