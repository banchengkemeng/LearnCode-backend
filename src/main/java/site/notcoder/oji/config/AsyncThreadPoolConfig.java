package site.notcoder.oji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncThreadPoolConfig {

    private static final int MAX_POOL_SIZE = 50;
    private static final int CORE_POOL_SIZE = 20;
    private static final int TASK_NUM = 200;
    private static final int ACTIVE_TIME = 60;

    @Bean
    public AsyncTaskExecutor judgeTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(TASK_NUM);
        threadPoolTaskExecutor.setKeepAliveSeconds(ACTIVE_TIME);
        threadPoolTaskExecutor.setThreadNamePrefix("judge-thread-pool");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
