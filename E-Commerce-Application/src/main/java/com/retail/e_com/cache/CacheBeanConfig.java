package com.retail.e_com.cache;


import com.retail.e_com.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheBeanConfig {
    @Bean
    CacheStore<String> otpCache(){
        return new CacheStore<String>(5);
    };
    @Bean
    CacheStore<User> userCache(){
        return new CacheStore<User>(30);
    };
}
