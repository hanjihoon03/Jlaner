package com.jlaner.project.config.outh2;

public interface Oauth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
