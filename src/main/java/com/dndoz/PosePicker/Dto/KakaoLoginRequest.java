package com.dndoz.PosePicker.Dto;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//ios
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "IOS 카카오 로그인 요청: KakaoLoginRequest")
public class KakaoLoginRequest {
	private Long uid;
	private String email;
	private String token;
}
