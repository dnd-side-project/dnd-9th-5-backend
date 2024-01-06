package com.dndoz.PosePicker.Controller;

import com.dndoz.PosePicker.Dto.KakaoLoginRequest;
import com.dndoz.PosePicker.Dto.LoginResponse;
import com.dndoz.PosePicker.Dto.PPTokenResponse;
import com.dndoz.PosePicker.Global.status.StatusResponse;
import com.dndoz.PosePicker.Service.AppleService;
import com.dndoz.PosePicker.Service.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(tags = {"유저 API"})
public class UserController {

    private final KakaoService kakaoService;
    private final AppleService appleService;

	//web 버전
    @ResponseBody
    @GetMapping("/login/oauth/kakao")
	@ApiOperation(value = "웹 카카오 로그인", notes = "웹 프론트 버전 카카오 로그인")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code){
        try{
            return ResponseEntity.ok(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }
    }

	//ios 버전
	@ResponseBody
	@GetMapping("/posepicker/token")
	@ApiOperation(value = "ios 포즈피커 토큰 생성", notes = "ios 카카오 로그인 전 포즈피커 토큰 생성")
	public ResponseEntity<PPTokenResponse> posePickerToken(){
		return ResponseEntity.ok(kakaoService.posePickerToken());
	}

	@ResponseBody
	@PostMapping("/login/ios/kakao")
	@ApiOperation(value = "ios 카카오 로그인", notes = "ios 버전 카카오 로그인")
	public ResponseEntity<LoginResponse> iosKakaoLogin(@RequestBody KakaoLoginRequest loginRequest){
		try{
			return ResponseEntity.ok(kakaoService.iosKakaoLogin(loginRequest));
		} catch (NoSuchElementException | IllegalAccessException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Exception");
		}
	}

	@ResponseBody
	@GetMapping("/login/ios/apple")
	@ApiOperation(value = "ios 애플 로그인", notes = "ios 버전 애플 로그인")
	public ResponseEntity<LoginResponse> appleLogin(@RequestParam String idToken){
		try{
			return ResponseEntity.ok(appleService.appleLogin(idToken));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
		}
	}

	@ResponseBody
	@DeleteMapping("/signOut")
	@ApiOperation(value = "탈퇴하기", notes = "북마크 정보 삭제 후 회원 탈퇴")
	public ResponseEntity<StatusResponse> signOut(
		@RequestHeader(value= "Authorization", required=false) String accessToken){
		try{
			return ResponseEntity.ok(kakaoService.signOut(accessToken));
		} catch (NoSuchElementException | IllegalAccessException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
		}
	}

}

