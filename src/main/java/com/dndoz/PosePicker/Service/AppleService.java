package com.dndoz.PosePicker.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.dndoz.PosePicker.Auth.AuthTokens;
import com.dndoz.PosePicker.Auth.AuthTokensGenerator;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.LoginResponse;
import com.dndoz.PosePicker.Repository.UserRepository;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleService {
	private final UserRepository userRepository;
	private final AuthTokensGenerator authTokensGenerator;

	//애플 로그인
	public LoginResponse appleLogin(String idToken) {

		//1. 아이디토큰으로 decode해서 사용자 정보 얻기
		HashMap<String, String> userInfo= userIdFromApple(idToken);

		//2. 회원가입 & 로그인 처리
		LoginResponse appleUserResponse= appleUserLogin(userInfo);

		return appleUserResponse;
	}

	public HashMap<String, String> userIdFromApple(String idToken) {
		HashMap<String, String> appleInfo= new HashMap<String,String>();

		//Jws<Claims> jws = null;

		StringBuffer result = new StringBuffer();
		try {
			URL url = new URL("https://appleid.apple.com/auth/keys");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";

			while ((line = br.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException ignored) {
		}

		JsonParser parser = new JsonParser();
		JsonObject keys = (JsonObject) parser.parse(result.toString());
		JsonArray keyArray = (JsonArray) keys.get("keys");


		String[] decodeArray = idToken.split("\\.");
		if (decodeArray.length != 3) {
			// JWT 형식이 아닌 경우 또는 잘못된 형식의 토큰인 경우 처리
			throw new IllegalStateException("Invalid JWT format");
		}
		String header = new String(Base64.getUrlDecoder().decode(decodeArray[0]));

		//apple에서 제공해주는 kid값과 일치하는지 알기 위해
		JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
		JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

		JsonObject avaliableObject = null;
		for (int i = 0; i < keyArray.size(); i++) {
			JsonObject appleObject = (JsonObject) keyArray.get(i);
			JsonElement appleKid = appleObject.get("kid");
			JsonElement appleAlg = appleObject.get("alg");

			if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
				avaliableObject = appleObject;
				break;
			}
		}

		//일치하는 공개키 없음
		if (ObjectUtils.isEmpty(avaliableObject))
			throw new IllegalStateException("일치하는 공개키가 없습니다.");

		//Public key 생성
		PublicKey publicKey = this.getPublicKey(avaliableObject);

		Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
		String userId = userInfo.getSubject();
		String email = userInfo.get("email", String.class);

		appleInfo.put("id", userId);
		appleInfo.put("email", email);
		appleInfo.put("nickname", "nickname");

		return appleInfo;
	}

	private LoginResponse appleUserLogin(HashMap<String, String> userInfo){
		Long uid= (long)((int)(Math.random() * 899999999) + 100000000);
		String iosId= userInfo.get("id").toString();
		String appleEmail = userInfo.get("email").toString();
		String nickName = userInfo.get("nickname").toString();

		User appleUser = userRepository.findByEmail(appleEmail).orElse(null);

		if (appleUser == null) {    //회원가입
			appleUser= new User();
			appleUser.setUid(uid);
			appleUser.setNickname("nickname");
			appleUser.setEmail(appleEmail);
			appleUser.setLoginType("apple");
			appleUser.setIosId(iosId);
			userRepository.save(appleUser);

			//토큰 생성
			AuthTokens token=authTokensGenerator.generate(appleEmail);
			return new LoginResponse(uid,nickName,appleEmail,token);
		} else { // 로그인
			AuthTokens token=authTokensGenerator.generate(appleEmail);
			return new LoginResponse(appleUser.getUid(),nickName,appleEmail,token);
		}
	}


	public PublicKey getPublicKey(JsonObject object) {
		String nStr = object.get("n").toString();
		String eStr = object.get("e").toString();

		byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
		byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

		BigInteger n = new BigInteger(1, nBytes);
		BigInteger e = new BigInteger(1, eBytes);

		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			return publicKey;
		} catch (Exception exception) {
			throw new IllegalStateException("Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다.");
		}
	}

}
