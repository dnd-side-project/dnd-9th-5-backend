package com.dndoz.PosePicker.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteAccountRequest {
	private String accessToken;
	private String refreshToken;
	private String withdrawalReason;
}
