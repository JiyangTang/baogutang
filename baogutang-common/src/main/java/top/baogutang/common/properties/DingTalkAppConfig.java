package top.baogutang.common.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author niko
 * @version 0.1.0
 * @create 2023/3/31 17:15
 * @since 0.1.0
 **/
@Component
@ConfigurationProperties(prefix = "dingtalk")
@Getter
@Setter
public class DingTalkAppConfig {

    private BaoGuTangProperties baoGuTang = new BaoGuTangProperties();

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BaoGuTangProperties {
        private String agentId;
        private String appKey;
        private String appSecret;
        private String appCorpId;
    }

}

