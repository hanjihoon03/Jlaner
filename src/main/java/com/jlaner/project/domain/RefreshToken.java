package com.jlaner.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken implements Serializable {

    @Id
    private String id;

    private Long memberId;

    private String accessToken;
    private String refreshToken;


    public RefreshToken(Long memberId, String accessToken, String refreshToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public RefreshToken refreshTokenUpdate(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
    public RefreshToken accessTokenUpdate(String newAccessToken) {
        this.refreshToken = newAccessToken;
        return this;
    }
}
