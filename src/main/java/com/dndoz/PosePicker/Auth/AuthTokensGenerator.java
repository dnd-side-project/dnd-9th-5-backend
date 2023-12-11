package com.dndoz.PosePicker.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일
	private static final long POSE_PICKER_TOKEN_EXPIRE_TIME = 1000 * 60; //1분

    private final JwtTokenProvider jwtTokenProvider;
	private final PPJwtTokenProvider ppJwtTokenProvider;

    //email or id 받아 Access Token 생성
    public AuthTokens generate(String uid) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        //String subject = email.toString();
        String accessToken = jwtTokenProvider.accessTokenGenerate(uid, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);

        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
	// Access Token 에서 uid 추출
	public String extractUid(String accessToken) {
		return jwtTokenProvider.extractUid(accessToken);
	}

	//ios 카톡 로그인 전에 포즈피커 자체 토큰 유효성
	public String posePickerGenerate() {
		long now = (new Date()).getTime();
		Date posePickerTokenExpiredAt = new Date(now + POSE_PICKER_TOKEN_EXPIRE_TIME);

		String posePickerToken = ppJwtTokenProvider.posePickerTokenGenerate(posePickerTokenExpiredAt);
		return posePickerToken;
	}
	//PosePicker Token 에서 subject 추출
	public String extractSubject(String token) {
		return ppJwtTokenProvider.extractSubject(token);
	}
}
