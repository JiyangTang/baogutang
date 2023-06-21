package top.baogutang.admin.config;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenConfigure {
    /**
     * 重写 Sa-Token 框架内部算法策略 
     */
    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略 
        SaStrategy.me.createToken = (loginId, loginType) -> {
            return SaFoxUtil.getRandomString(60);
        };
    }
}
