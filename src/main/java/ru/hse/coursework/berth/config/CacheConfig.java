package ru.hse.coursework.berth.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String HAS_CHAT_ACCESS = "hasChatAccess";

    @Bean
    Cache cacheHasChatAccess() {
        return new CaffeineCache(HAS_CHAT_ACCESS,
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .build()
        );
    }
}
