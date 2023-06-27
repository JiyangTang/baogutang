package top.baogutang.common.properties;

import org.springframework.stereotype.Component;

/**
 * @author niko
 * @version 0.1.0
 * @create 2023/3/31 19:25
 * @since 0.1.0
 **/
@Component
public class BaoGuTangConfigStrategy implements DingTalkConfigStrategy {
    private String agentId;
    private String appKey;
    private String appSecret;
    private String appCorpId;
    private String appSource;

    public BaoGuTangConfigStrategy() {
    }

    public BaoGuTangConfigStrategy(String agentId, String appKey, String appSecret, String appCorpId, String appSource) {
        this.agentId = agentId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.appCorpId = appCorpId;
        this.appSource = appSource;
    }

    @Override
    public String getAgentId() {
        return agentId;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    @Override
    public String getAppSecret() {
        return appSecret;
    }

    @Override
    public String getAppCorpId() {
        return appCorpId;
    }

    @Override
    public String getAppSource() {
        return appSource;
    }
}
