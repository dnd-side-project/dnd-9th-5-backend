package com.dndoz.PosePicker.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dndoz.PosePicker.Auth.AuthTokens;
import com.dndoz.PosePicker.Service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Api(tags = {"토큰 재발급 API"})
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/reissue-token")
	@ApiOperation(value = "토큰 재발급", notes = "accessToken 만료 시 refreshToken 으로 재발급")
	public ResponseEntity<AuthTokens> reissueToken(
		@RequestHeader(value= "Authorization", required=false) String refreshToken) throws IllegalAccessException {
		try {
			return ResponseEntity.ok(authService.reissueToken(refreshToken));
		} catch ( IllegalAccessException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
		}
	}
}
