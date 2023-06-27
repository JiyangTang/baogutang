package top.baogutang.admin.utils;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.properties.DingTalkConfigFactory;
import top.baogutang.common.properties.DingTalkConfigStrategy;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.baogutang.common.constants.CacheConstant.PREFIX_DING_TALK_ACCESS_TOKEN;
import static top.baogutang.common.constants.DingTalkServerConstants.ACC_TOKEN;
import static top.baogutang.common.constants.DingTalkServerConstants.CORP_CONVERSATION_ASYNC_SEND_V2;


/**
 * @description: 钉钉消息推送工具类
 * @author: nikooh
 * @date: 2023/04/03 : 16:29
 */
@Slf4j
@Component
public class DingTalkMsgPushUtils {

    @Resource
    private DingTalkConfigFactory dingTalkConfigFactory;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 钉钉消息推送 {https://open.dingtalk.com/document/orgapp/work-notice-option}
     *
     * @param agentId    应用ID
     * @param deptIdList 消息接收部门ID集合
     * @param userIdList 消息接收用户ID集合
     * @param message    具体消息内容
     * @return 消息ID
     */
    public Long dingTalkMsgPush(String agentId, Boolean toAllUser, List<String> deptIdList, List<String> userIdList, OapiMessageCorpconversationAsyncsendV2Request.Msg message) {

        // 1.获取消息所属应用及应用token
        String accessToken = this.getAccessToken(agentId);
        if (StringUtils.isBlank(accessToken)) {
            throw new BusinessException("消息发送失败->未能成功生成应用token");
        }

        // 2.组装dingTalk请求参数
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(Long.parseLong(agentId));
        request.setToAllUser(toAllUser);
        if (CollectionUtils.isNotEmpty(deptIdList)) {
            request.setDeptIdList(StringUtils.join(deptIdList.toArray(), ","));
        }
        if (CollectionUtils.isNotEmpty(userIdList)) {
            request.setUseridList(StringUtils.join(userIdList.toArray(), ","));
        }
        request.setMsg(message);

        // 3.请求钉钉发送消息
        DingTalkClient client = new DefaultDingTalkClient(CORP_CONVERSATION_ASYNC_SEND_V2);
        OapiMessageCorpconversationAsyncsendV2Response response = null;
        try {
            response = client.execute(request, accessToken);
            log.info(">>>>>>>>>>request dingTalk send msg res:{}<<<<<<<<<<", JSON.toJSONString(response));
            return response.getTaskId();
        } catch (Exception e) {
            log.error(">>>>>>>>>>request dingTalk send msg error:{}<<<<<<<<<<", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取钉钉应用accessToken
     *
     * @param agentId 应用ID
     * @return token
     */
    public String getAccessToken(String agentId) {
        DingTalkConfigStrategy config = dingTalkConfigFactory.getConfigByAgentId(agentId);
        String appKey = config.getAppKey();
        String appSecret = config.getAppSecret();
        String cacheKey = PREFIX_DING_TALK_ACCESS_TOKEN + appKey;
        Object accessTokenObj = redisTemplate.opsForValue().get(cacheKey);
        if (Objects.nonNull(accessTokenObj)) {
            return (String) accessTokenObj;
        }
        // 缓存不存在，生成token
        DingTalkClient client = new DefaultDingTalkClient(ACC_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response;
        try {
            response = client.execute(request);
            log.info(">>>>>>>>>>request dingTalk gene accessToken,request:[{}] response:[{}]<<<<<<<<<<", JSON.toJSONString(request), JSON.toJSONString(response));
            if (Objects.nonNull(response) && Objects.nonNull(response.getAccessToken())) {
                redisTemplate.opsForValue().set(cacheKey, response.getAccessToken(), 7000L, TimeUnit.SECONDS);
                return response.getAccessToken();
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>request dingTalk gene accessToken error! request:{},errorMsg:{}<<<<<<<<<<", JSON.toJSONString(request), e.getMessage(), e);
        }
        return null;
    }

}
