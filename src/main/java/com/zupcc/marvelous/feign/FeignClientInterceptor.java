package com.zupcc.marvelous.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${marvel.api.private-key}")
    private String privateKey;

    @Value("${marvel.api.public-key}")
    private String publicKey;

    private static final String TIMESTAMP = "ts";
    private static final String HASH = "hash";
    private static final String API_KEY = "apikey";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String ts = "1";
        requestTemplate.query(TIMESTAMP, ts).query(HASH, generateHash(ts))
                .query(API_KEY, publicKey);
    }

    private String generateHash(String ts) {
        return DigestUtils.md5DigestAsHex(
                (ts + privateKey + publicKey).getBytes(StandardCharsets.UTF_8));
    }
}