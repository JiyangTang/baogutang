package top.baogutang.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.baogutang.common.domain.Results;

/**
 * @description: 回调控制器
 * @author: nikooh
 * @date: 2023/06/16 : 10:10
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/callback")
public class CallbackController {


    @RequestMapping("/wx/msgPush")
    public Results<String> wxMsgPushCallback() {
        return Results.ok(null);
    }
}
