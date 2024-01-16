package com.dndoz.PosePicker.Auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class PPJwtTokenProvider {

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
}
