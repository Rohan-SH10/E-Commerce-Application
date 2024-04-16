package com.retail.e_com.cache;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;

public class CacheStore<T> {
    private Cache<String,T> cache;

    public CacheStore(int maxAge){
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(maxAge))
                .concurrencyLevel(Runtime.getRuntime()
                        .availableProcessors())
                .build();

    }

    public void add(String key, T value){
        cache.put(key,value);
    }

    public T getCache(String key) {
        T data =cache.getIfPresent(key);
        return data;
    }

    public void remove(String key){
        cache.invalidate(key);
    }
}
