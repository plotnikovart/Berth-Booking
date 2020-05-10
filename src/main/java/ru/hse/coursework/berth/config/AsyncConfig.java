package ru.hse.coursework.berth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    public static final String RATING_CALCULATOR_EXECUTOR = "BerthRatingCalculatorExecutor";

    @Bean(name = RATING_CALCULATOR_EXECUTOR)
    Executor berthRatingCalculatorExecutor() {
        var tp = new ThreadPoolTaskExecutor();
        tp.setCorePoolSize(1);
        tp.setMaxPoolSize(1);
        tp.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        tp.setThreadNamePrefix(RATING_CALCULATOR_EXECUTOR);
        return tp;
    }
}
