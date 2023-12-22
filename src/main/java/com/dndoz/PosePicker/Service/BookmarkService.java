package com.dndoz.PosePicker.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Auth.JwtTokenProvider;
import com.dndoz.PosePicker.Domain.Bookmark;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.BookmarkResponse;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Repository.BookmarkRepository;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService{

	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final PoseInfoRepository poseInfoRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${aws.image_url.prefix}")
	private String urlPrefix;

	//북마크 등록
	@Transactional
	public BookmarkResponse insert(String accessToken, Long poseId) throws Exception {
		String token=jwtTokenProvider.extractJwtToken(accessToken);
		//System.out.println("@@@@@@@Bookmark 39행 accesstoken:" + token);
		if (! jwtTokenProvider.validateToken(token)) {
			throw new IllegalAccessException("유효한 토큰이 아닙니다.");
		}

		Long userId= Long.valueOf(jwtTokenProvider.extractUid(token));
		User user=userRepository.findById(userId).orElseThrow(NullPointerException::new);
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(poseId).orElseThrow(NullPointerException::new);

		//북마크 없으면 등록
		if (bookmarkRepository.findByUserAndPoseInfo(user, poseInfo).isEmpty()){
			Bookmark bookmark = new Bookmark(user, poseInfo);
			bookmarkRepository.save(bookmark);

			BookmarkResponse response= new BookmarkResponse();
			response.setPoseId(poseInfo.getPoseId());
			return response;
		}else{
			throw new Exception();
		}
	}


	//북마크 취소
	@Transactional
	public BookmarkResponse delete(String accessToken, Long poseId) throws Exception {
		String token=jwtTokenProvider.extractJwtToken(accessToken);
		if (! jwtTokenProvider.validateToken(token)) {
			throw new IllegalAccessException("유효한 토큰이 아닙니다.");
		}

		Long userId= Long.valueOf(jwtTokenProvider.extractUid(token));
		User user=userRepository.findById(userId).orElseThrow(NullPointerException::new);
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(poseId).orElseThrow(NullPointerException::new);

		//북마크 있으면 북마크 취소
		if (bookmarkRepository.findByUserAndPoseInfo(user, poseInfo).isPresent()){
			bookmarkRepository.deleteByUserAndPoseInfo(user,poseInfo);

			BookmarkResponse response= new BookmarkResponse();
			response.setPoseId(-1L);
			return response;
		} else{
			throw new Exception();
		}
	}

	// 북마크 피드 리스트 스크롤
	@Transactional(readOnly = true)
	public Slice<PoseInfoResponse> findBookmark(String accessToken, final Integer pageNumber, final Integer pageSize) throws
		IllegalAccessException {
		String token=jwtTokenProvider.extractJwtToken(accessToken);
		if (! jwtTokenProvider.validateToken(token)) {
			throw new IllegalAccessException("유효한 토큰이 아닙니다.");
		}
		Long uid= Long.valueOf(jwtTokenProvider.extractUid(token));
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return poseInfoRepository.findBookmark(uid,pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

}
