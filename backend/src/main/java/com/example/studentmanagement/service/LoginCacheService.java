package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.LoginInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginCacheService {
    private final Cache cache;

    public LoginCacheService(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("authCache");
    }

    public void cacheLogin(String username, List<String> roles) {
        if (cache == null) return;
        LoginInfo info = new LoginInfo();
        info.setUsername(username);
        info.setRoles(roles);
        cache.put(username, info);
    }

    public LoginInfo get(String username) {
        if (cache == null) return null;
        Cache.ValueWrapper vw = cache.get(username);
        return vw != null ? (LoginInfo) vw.get() : null;
    }

    public void evict(String username) {
        if (cache == null) return;
        cache.evict(username);
    }
}