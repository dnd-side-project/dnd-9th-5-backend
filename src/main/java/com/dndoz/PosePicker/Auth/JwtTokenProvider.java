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
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Key key;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String secretKey) {
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
	public String refreshTokenGenerate(Date expiredAt) {
		return Jwts.builder()
			.setExpiration(expiredAt)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
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
}
