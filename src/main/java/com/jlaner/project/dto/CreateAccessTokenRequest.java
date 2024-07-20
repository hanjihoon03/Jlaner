package com.jlaner.project.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateAccessTokenRequest {
    private String refreshToken;
}
