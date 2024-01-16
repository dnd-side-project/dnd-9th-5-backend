package com.dndoz.PosePicker.Auth;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class PPJwtTokenProvider {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Key pz_key;

	public PPJwtTokenProvider(@Value("${custom.jwt.posepickerKey}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.pz_key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String posePickerTokenGenerate(Date expiredAt) {
		return Jwts.builder()
			.setSubject("posePickerLogin")
			.setExpiration(expiredAt)
			.signWith(pz_key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String extractSubject(String token) {
		Claims claims = parseClaims2(token);
		return claims.getSubject();
	}

	private Claims parseClaims2(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(pz_key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	//유효한 토큰인지 확인
	public boolean validateToken(String token) throws IllegalAccessException{
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(pz_key).parseClaimsJws(token);
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

}
