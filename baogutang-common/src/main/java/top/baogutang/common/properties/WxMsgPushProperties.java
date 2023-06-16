package top.baogutang.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/16 : 12:50
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "wx.msg-push")
public class WxMsgPushProperties {

    private String appToken;

    private Set<Long> topicIds;
}
