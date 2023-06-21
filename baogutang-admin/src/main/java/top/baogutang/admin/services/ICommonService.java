package top.baogutang.admin.services;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/19 : 12:20
 */
public interface ICommonService {

    /**
     * 获取验证码
     *
     * @param key 验证码key
     * @return 验证码
     */
    String randomImage(String key);
}
