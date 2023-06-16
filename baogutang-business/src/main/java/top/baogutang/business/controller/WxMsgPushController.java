package top.baogutang.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import top.baogutang.business.services.IWxMsgPushService;

import javax.annotation.Resource;

/**
 * @description: 微信消息推送控制器
 * @author: nikooh
 * @date: 2023/06/16 : 10:24
 */
@Slf4j
@Controller
@RequestMapping("/api/v1/wx/msgPush")
public class WxMsgPushController {

    @Resource
    private IWxMsgPushService wxMsgPushService;


    /**
     * 创建一个带参数的二维码，用户扫码的时候，回调里面会携带二维码的参数.
     *
     * @return 二维码
     */
    @GetMapping("/qrCode")
    public RedirectView getQrCode() {
        return new RedirectView(wxMsgPushService.getOrCode());
    }
}
