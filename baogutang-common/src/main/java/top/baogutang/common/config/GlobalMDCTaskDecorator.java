package top.baogutang.common.config;

import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @description: 多线程链路ID
 * @author: nikooh
 * @date: 2023/06/15 : 12:20
 */
@Configuration
public class GlobalMDCTaskDecorator implements TaskDecorator, BeanPostProcessor {


    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (mdcContext != null) {
                    MDC.setContextMap(mdcContext);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ThreadPoolTaskExecutor) {
            ((ThreadPoolTaskExecutor) bean).setTaskDecorator(this);
        }
        return bean;
    }

}
