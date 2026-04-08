package com.safetyalertsystem.service.authentication;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpCacheService {

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public void store(String phone, String otp) {
        cache.put(phone, otp);
    }

    public String get(String phone) {
        return cache.get(phone);
    }

    public void remove(String phone) {
        cache.remove(phone);
    }
}