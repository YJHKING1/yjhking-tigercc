package org.yjhking.tigercc.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author YJH
 */
@Data
@Component
@ConfigurationProperties(prefix = "tigercc.message")
@RefreshScope
public class MessageProperties {
    private String content;
    private String title;
    private String type;
}
