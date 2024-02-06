package com.dndoz.PosePicker.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {
	private String accessToken;
	private String refreshToken;
}
