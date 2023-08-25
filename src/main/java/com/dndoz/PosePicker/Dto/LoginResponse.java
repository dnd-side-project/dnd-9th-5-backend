package com.dndoz.PosePicker.Dto;

import com.dndoz.PosePicker.Auth.AuthTokens;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nickname;
    private String email;
    private AuthTokens token;

    public LoginResponse(Long id, String nickname, String email, AuthTokens token) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.token = token;
    }
}
