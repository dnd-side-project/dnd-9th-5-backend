package com.dndoz.PosePicker.Controller;

import com.dndoz.PosePicker.Dto.LoginResponse;
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

}

