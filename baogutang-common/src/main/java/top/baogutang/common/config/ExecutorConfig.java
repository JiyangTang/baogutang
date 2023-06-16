package top.baogutang.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 线程池配置
 * @author: nikooh
 * @date: 2023/06/15 : 12:18
 */
@Slf4j
@Configuration
public class ExecutorConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${thread.pool.core.pool.size:10}")
    private Integer corePoolSize;

    @Value("${thread.pool.max.pool.size:20}")
    private Integer maxPoolSize;

    @Value("${thread.pool.keep.alive.second:10}")
    private Integer keepAliveSecond;

    @Value("${thread.pool.queue.capacity:200}")
    private Integer queueCapacity;

    @Bean("commonExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(appName);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSecond);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
