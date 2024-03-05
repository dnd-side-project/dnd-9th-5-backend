package com.dndoz.PosePicker.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.dndoz.PosePicker.Global.error.exception.CustomTokenException;

@Component
public class JwtTokenProvider {
	private final RedisTemplate<String, String> redisTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Key key;
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String secretKey,
		RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String accessTokenGenerate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)	//uid
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
	public String refreshTokenGenerate(String subject, Date expiredAt) {
		String refreshToken= Jwts.builder()
			.setExpiration(expiredAt)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		// redis에 저장
		redisTemplate.opsForValue().set(
			"refresh:"+refreshToken,
			subject,
			REFRESH_TOKEN_EXPIRE_TIME,
			TimeUnit.MILLISECONDS
		);

		return refreshToken;
	}

	public String extractUid(String accessToken) {
		Claims claims = parseClaims(accessToken);
		return claims.getSubject();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	//유효한 토큰인지 확인
	public boolean validateToken(String token) throws IllegalAccessException{
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			//Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
			if (hasKeyBlackList(token)){
				throw new CustomTokenException("로그아웃/탈퇴 된 토큰 입니다");
			}
			return !claims.getBody().getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			// 토큰이 만료된 경우
			logger.error("Expired JWT token: {}", e.getMessage());
			throw e;
		} catch (UnsupportedJwtException e) {
			// 지원되지 않는 토큰
			logger.error("Unsupported JWT token: {}", e.getMessage());
			throw e;
		} catch (MalformedJwtException e) {
			// 잘못된 jwt 구조 토큰
			logger.error("Malformed JWT token: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			//throw new IllegalAccessException("유효한 토큰이 아닙니다.");
			throw e;
		}
	}

	// 헤더에서 Bearer 토큰 부분 추출하는 메서드
	public String extractJwtToken(String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}

	//redis key(refreshToken) 값을 찾아 value (uid)를 반환하는 메서드 (재발급, 로그아웃)
	public String findRefreshToken(String refreshToken) {
		return redisTemplate.opsForValue().get("refresh:"+refreshToken);
	}

	//로그아웃 된 accessToken 여부 확인
	private boolean hasKeyBlackList(String token) {
		return redisTemplate.hasKey(token);
	}

	//JWT 토큰의 만료시간
	public Long getExpiration(String accessToken){
		Date expiration = Jwts.parserBuilder().setSigningKey(key)
			.build().parseClaimsJws(accessToken).getBody().getExpiration();
		long now = new Date().getTime();
		return expiration.getTime() - now;
	}
}
