package com.dndoz.PosePicker.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.Bookmark;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.BookmarkRequest;
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

	@Value("${aws.image_url.prefix}")
	private String urlPrefix;

	//북마크 등록
	@Transactional
	public void insert(BookmarkRequest bookmarkDto) throws Exception {
		User user=userRepository.findById(bookmarkDto.getUid()).orElseThrow(NullPointerException::new);
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(bookmarkDto.getPoseId()).orElseThrow(NullPointerException::new);

		//북마크 없으면 등록
		if (bookmarkRepository.findByUserAndPoseInfo(user, poseInfo).isEmpty()){
			Bookmark bookmark = new Bookmark(user, poseInfo);
			bookmarkRepository.save(bookmark);
		}

	}

	//북마크 취소
	@Transactional
	public void delete(BookmarkRequest bookmarkDto) throws Exception {
		User user= userRepository.findById(bookmarkDto.getUid()).orElseThrow(NullPointerException::new);
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(bookmarkDto.getPoseId()).orElseThrow(NullPointerException::new);

		//북마크 있으면 북마크 취소
		if (bookmarkRepository.findByUserAndPoseInfo(user, poseInfo).isPresent()){
			bookmarkRepository.deleteByUserAndPoseInfo(user,poseInfo);
		} else{
			throw new Exception();
		}
	}


	// 북마크 피드 리스트 스크롤
	@Transactional(readOnly = true)
	public Slice<PoseInfoResponse> findBookmark(final String uid, final Integer pageNumber, final Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return poseInfoRepository.findBookmark(uid,pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

}
