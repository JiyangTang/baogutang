package top.baogutang.common.properties;

/**
 * @author niko
 * @version 0.1.0
 * @create 2023/3/31 19:23
 * @since 0.1.0
 **/
public interface DingTalkConfigStrategy {

    String getAgentId();

    String getAppKey();

    String getAppSecret();

    String getAppCorpId();

    String getAppSource();

}