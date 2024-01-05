package com.dndoz.PosePicker.Service;

import com.dndoz.PosePicker.Auth.AuthTokens;
import com.dndoz.PosePicker.Auth.AuthTokensGenerator;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.KakaoLoginRequest;
import com.dndoz.PosePicker.Dto.LoginResponse;
import com.dndoz.PosePicker.Dto.PPTokenResponse;
import com.dndoz.PosePicker.Repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;

	/** [1] ios 버전 카카오 로그인 **/
	//포즈피커 자체 토큰 생성 후 전달
	public PPTokenResponse posePickerToken(){
		String token= authTokensGenerator.posePickerGenerate();
		PPTokenResponse tokenDto= new PPTokenResponse();
		tokenDto.setToken(token);
		return tokenDto;
	}

	public LoginResponse iosKakaoLogin(KakaoLoginRequest loginRequest) {
		Long uid=loginRequest.getUid();
		String email= loginRequest.getEmail();
		String subject= authTokensGenerator.extractSubject(loginRequest.getToken());

		if (subject.equals("posePickerLogin")){
			User kakaoUser = userRepository.findById(loginRequest.getUid())
				.orElse(null);

			if (kakaoUser == null) {    //회원가입
				kakaoUser= new User();
				kakaoUser.setUid(uid);
				kakaoUser.setNickname("nickname");
				kakaoUser.setEmail(email);
				kakaoUser.setLoginType("kakao");
				userRepository.save(kakaoUser);
			}
			//토큰 생성
			AuthTokens token=authTokensGenerator.generate(loginRequest.getUid().toString());
			return new LoginResponse(uid,"nickname",email,token);

		}else{
			return null;
		}

	}

	/** [2] Web 버전 카카오 로그인 **/
    public LoginResponse kakaoLogin(String code) {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo= getKakaoUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponse kakaoUserResponse= kakaoUserLogin(userInfo);

        return kakaoUserResponse;
    }

    @Value("${kakao.key.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    //1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText();
    }

    //2. 토큰으로 카카오 API 호출
    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
            //System.out.println(jsonNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }

    //3. 카카오ID로 회원가입 & 로그인 처리
    private LoginResponse kakaoUserLogin(HashMap<String, Object> userInfo){

        Long uid= Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        User kakaoUser = userRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {    //회원가입
        	kakaoUser= new User();
        	kakaoUser.setUid(uid);
        	kakaoUser.setNickname(nickName);
        	kakaoUser.setEmail(kakaoEmail);
        	kakaoUser.setLoginType("kakao");
            userRepository.save(kakaoUser);
        }
        //토큰 생성
        AuthTokens token=authTokensGenerator.generate(uid.toString());
        return new LoginResponse(uid,nickName,kakaoEmail,token);
    }

}
