package top.baogutang.common.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author niko
 * @version 0.1.0
 * @create 2023/3/31 19:25
 * @since 0.1.0
 **/
@Component
public class DingTalkConfigFactory {

    public static final String BAO_GU_TANG = "BAO_GU_TANG";

    private Map<String, DingTalkConfigStrategy> configMap;

    @Qualifier
    public DingTalkAppConfig dingTalkAppConfig;

    @Autowired
    public DingTalkConfigFactory(DingTalkAppConfig dingTalkAppConfig) {
        this.dingTalkAppConfig = dingTalkAppConfig;
        this.configMap = new HashMap<>();
        this.configMap.put(BAO_GU_TANG, new BaoGuTangConfigStrategy(dingTalkAppConfig.getBaoGuTang().getAgentId(),
                dingTalkAppConfig.getBaoGuTang().getAppKey(), dingTalkAppConfig.getBaoGuTang().getAppSecret(),
                dingTalkAppConfig.getBaoGuTang().getAppCorpId(), BAO_GU_TANG));
    }

    public DingTalkConfigStrategy getConfig(String appType) {
        return configMap.get(appType);
    }

    public DingTalkConfigStrategy getConfigByAgentId(String agentId) {
        if (Objects.equals(agentId, dingTalkAppConfig.getBaoGuTang().getAgentId())) {
            return getConfig(BAO_GU_TANG);
        }
        return null;
    }
}
