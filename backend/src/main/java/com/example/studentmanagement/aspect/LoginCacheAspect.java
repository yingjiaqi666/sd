package com.example.studentmanagement.aspect;

import com.example.studentmanagement.service.LoginCacheService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoginCacheAspect {

    private final LoginCacheService cacheService;

    public LoginCacheAspect(LoginCacheService cacheService) {
        this.cacheService = cacheService;
    }

    // 拦截 Spring Security 认证成功（ProviderManager.authenticate 返回 Authentication）
    @AfterReturning(pointcut = "execution(* org.springframework.security.authentication.ProviderManager.authenticate(..))",
                    returning = "auth")
    public void cacheOnAuthSuccess(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            List<String> roles = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .map(s -> s.startsWith("ROLE_") ? s.substring(5) : s)
                    .collect(Collectors.toList());
            cacheService.cacheLogin(username, roles);
        }
    }
}