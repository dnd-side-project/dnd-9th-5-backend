package com.dndoz.PosePicker.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dndoz.PosePicker.Auth.AuthTokens;
import com.dndoz.PosePicker.Auth.AuthTokensGenerator;
import com.dndoz.PosePicker.Auth.JwtTokenProvider;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Global.error.exception.CustomTokenException;
import com.dndoz.PosePicker.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {
	private final AuthTokensGenerator authTokensGenerator;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RedisTemplate<String, String> redisTemplate;

	public AuthTokens reissueToken(String refreshToken) throws IllegalAccessException {

		String token=jwtTokenProvider.extractJwtToken(refreshToken);
		if (!jwtTokenProvider.validateToken(token)){
			return null;
		}

		//재발급하기 위해 일치하는 refresh 토큰 찾아서 uid 값 가져오기
		String uid = jwtTokenProvider.findRefreshToken(token);
		if (uid == null) {
			throw new CustomTokenException("일치하는 Refresh Token이 존재하지 않습니다");
		}

		redisTemplate.delete("refresh:"+token);
		User user=userRepository.findById(Long.valueOf(uid)).orElseThrow(NullPointerException::new);

		// uid 매개변수로 token 다시 생성
		AuthTokens newCreatedToken=authTokensGenerator.generate(user.getUid().toString());

		return newCreatedToken;
	}
}
