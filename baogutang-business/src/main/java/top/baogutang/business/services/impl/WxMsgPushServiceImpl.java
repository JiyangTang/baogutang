package top.baogutang.business.services.impl;

import com.alibaba.fastjson.JSON;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeReq;
import com.zjiecode.wxpusher.client.bean.CreateQrcodeResp;
import com.zjiecode.wxpusher.client.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.baogutang.business.services.IWxMsgPushService;
import top.baogutang.common.constants.ErrorCodeEnum;
import top.baogutang.common.exceptions.BusinessException;
import top.baogutang.common.properties.WxMsgPushProperties;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.baogutang.common.constants.CacheConstant.WX_MSG_PUSH_QR_CODE_PREFIX;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/16 : 10:27
 */
@Slf4j
@RefreshScope
@Service
public class WxMsgPushServiceImpl implements IWxMsgPushService {

    /**
     * 二维码有效时间
     */
    private static final Integer QR_CODE_EXPIRE_TIME = 10 * 24 * 60 * 60;

    @Resource
    private WxMsgPushProperties wxMsgPushProperties;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getOrCode() {
        String cacheKey = String.format(WX_MSG_PUSH_QR_CODE_PREFIX, wxMsgPushProperties.getAppToken());
        Object cacheUrlObj = redisTemplate.opsForValue().get(cacheKey);
        if (Objects.nonNull(cacheUrlObj)) {
            return (String) cacheUrlObj;
        }
        CreateQrcodeReq createQrcodeReq = new CreateQrcodeReq();
        //必填，应用的appToken
        createQrcodeReq.setAppToken(wxMsgPushProperties.getAppToken());
        //必填，携带的参数
        createQrcodeReq.setExtra("Niko's MP");
        //可选，二维码有效时间，默认1800 s，最大30天，单位是s
        createQrcodeReq.setValidTime(QR_CODE_EXPIRE_TIME);
        Result<CreateQrcodeResp> respResult = null;
        try {
            respResult = WxPusher.createAppTempQrcode(createQrcodeReq);
        } catch (Exception e) {
            log.error(">>>>>>>>>>创建二维码异常：{}<<<<<<<<<<", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
        }
        log.info(">>>>>>>>>>创建临时二维码请求参数：{}，响应参数：{}<<<<<<<<<<", JSON.toJSONString(createQrcodeReq), JSON.toJSONString(respResult));
        if (respResult.isSuccess()) {
            //创建成功
            CreateQrcodeResp createQrcodeResp = respResult.getData();
            redisTemplate.opsForValue().set(cacheKey, createQrcodeResp.getUrl(), QR_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
            return createQrcodeResp.getUrl();
        }
        throw new BusinessException(ErrorCodeEnum.E_BIZ_ERROR);
    }
}
