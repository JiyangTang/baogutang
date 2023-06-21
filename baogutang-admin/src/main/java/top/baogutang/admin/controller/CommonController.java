package top.baogutang.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.baogutang.admin.services.ICommonService;
import top.baogutang.common.domain.Results;

import javax.annotation.Resource;

/**
 * @description: 通用能力控制器
 * @author: nikooh
 * @date: 2023/06/19 : 12:17
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    @Resource
    private ICommonService commonService;


    /**
     * 获取验证码
     *
     * @param key 验证码key
     * @return 验证码
     */
    @GetMapping("/randomImage/{key}")
    public Results<String> randomImage(@PathVariable("key") String key) {
        return Results.ok(commonService.randomImage(key));
    }
}
