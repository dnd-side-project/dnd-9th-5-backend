package com.dndoz.PosePicker.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.dndoz.PosePicker.Auth.JwtTokenProvider;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Domain.PoseTalk;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.PoseFeedRequest;
import com.dndoz.PosePicker.Dto.PoseFeedResponse;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTagAttributeResponse;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import com.dndoz.PosePicker.Dto.PoseUploadRequest;
import com.dndoz.PosePicker.Repository.BookmarkRepository;
import com.dndoz.PosePicker.Repository.PoseFilterRepository;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.PoseTagAttributeRepository;
import com.dndoz.PosePicker.Repository.PoseTalkRepository;
import com.dndoz.PosePicker.Repository.UserRepository;

@Transactional(readOnly = true)
@Service
public class PoseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final AmazonS3 amazonS3;
	private final UserRepository userRepository;
	private final PoseInfoRepository poseInfoRepository;
	private final PoseTalkRepository poseTalkRepository;
	private final PoseFilterRepository poseFilterRepository;
	private final PoseTagAttributeRepository poseTagAttributeRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final BookmarkRepository bookmarkRepository;

	@Value("${aws.image_url.prefix}")
	private String urlPrefix;

	private List<PoseInfo> filteredPoseInfo;
	private List<PoseInfo> recommendedPoseInfo;
	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;

	public PoseService(AmazonS3 amazonS3,
		final UserRepository userRepository,
	final PoseInfoRepository poseInfoRepository,
		final PoseTalkRepository poseTalkRepository,
		final PoseFilterRepository poseFilterRepository,
		final PoseTagAttributeRepository poseTagAttributeRepository,
		final JwtTokenProvider jwtTokenProvider, final BookmarkRepository bookmarkRepository) {
		this.amazonS3 = amazonS3;
		this.userRepository = userRepository;
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseFilterRepository = poseFilterRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.bookmarkRepository = bookmarkRepository;
	}

	//포즈 이미지 상세 조회
	public PoseInfoResponse getPoseInfo(String accessToken, Long pose_id) throws IllegalAccessException {
		PoseInfo poseInfo = poseFilterRepository.findByPoseId(pose_id).orElseThrow(NullPointerException::new);

		if (null != accessToken) {
			String token = jwtTokenProvider.extractJwtToken(accessToken);
			if (!jwtTokenProvider.validateToken(token)) {
				return null;
			}
			Long userId = Long.valueOf(jwtTokenProvider.extractUid(token));
			boolean bookmarkCheck = bookmarkRepository.existsByUserIdAndPoseId(userId, pose_id).intValue() > 0;
			poseInfo.setBookmarkCheck(bookmarkCheck);
		}
		return new PoseInfoResponse(urlPrefix, poseInfo);
	}

	public String uploadPose(String accessToken, PoseUploadRequest poseDto, MultipartFile multipartFile) throws
		IOException, IllegalAccessException {
		System.out.println(accessToken);
		String token = jwtTokenProvider.extractJwtToken(accessToken);
		if (!jwtTokenProvider.validateToken(token)) {
			return null;
		}
		Long userId = Long.valueOf(jwtTokenProvider.extractUid(token));
		User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);

		if (!multipartFile.isEmpty()) {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(multipartFile.getSize());
			metadata.setContentType(multipartFile.getContentType());

			String fileType = (multipartFile.getContentType()).substring(6);  //ex) image/png -> png

			String uploadFileName =
				poseDto.getFrameCount() + "[pz]" + poseDto.getFrameCount() + "[pz]" + poseDto.getTags() + "[pz]"
					+ poseDto.getSource() + "[pz]" + poseDto.getSourceUrl() + "[pz]" + poseDto.getDescription()
					+ ".jpg";

			System.out.println(uploadFileName);
			amazonS3.putObject(bucketName, uploadFileName, multipartFile.getInputStream(), metadata);
			return amazonS3.getUrl(bucketName, uploadFileName).toString();
		} else
			return "null";
	}

	//포즈픽(사진) 조회
	public PoseInfoResponse showRandomPoseInfo(Long people_count) {
		PoseInfo poseInfo = poseFilterRepository.findRandomPoseInfo(people_count)
			.orElseThrow(NullPointerException::new);
		return new PoseInfoResponse(urlPrefix, poseInfo);
	}

	//포즈톡(단어) 조회
	public PoseTalkResponse findRandomPoseTalk() {
		PoseTalk poseWord = poseTalkRepository.findRandomPoseTalk();
		return new PoseTalkResponse(poseWord);
	}

	//포즈 태그 속성 조회
	public PoseTagAttributeResponse findPoseTagAttribute() {
		List<PoseTagAttribute> poseTagAttributes = poseTagAttributeRepository.findPoseTagAttribute();
		return new PoseTagAttributeResponse(poseTagAttributes);
	}

	@Transactional
	public void setBookmarkStatusForPoses(Long userId) {
		//한 번의 조회로 여러 포즈에 대한 북마크 여부를 설정
		List<Object[]> bookmarkStatusList = poseInfoRepository.findBookmarkStatusByUserId(userId);
		Map<Long, Boolean> bookmarkStatusMap = new HashMap<>();

		for (Object[] result : bookmarkStatusList) {
			Long poseId = Long.valueOf(result[0].toString());
			boolean isBookmarked = ((BigInteger)result[1]).compareTo(BigInteger.ZERO) != 0;
			bookmarkStatusMap.put(poseId, isBookmarked);
		}
		List<PoseInfo> poses = poseInfoRepository.findPosesWithBookmarkStatus(userId);
		for (PoseInfo pose : poses) {
			boolean isBookmarked = bookmarkStatusMap.getOrDefault(pose.getPoseId(), false);
			pose.setBookmarkCheck(isBookmarked);
		}
		poseInfoRepository.saveAll(poses);
	}

	@Transactional(readOnly = true)
	public Slice<PoseInfoResponse> findPoses(String accessToken, final Integer pageNumber,
		final Integer pageSize) throws IllegalAccessException {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		if (null != accessToken) {
			String token = jwtTokenProvider.extractJwtToken(accessToken);
			if (!jwtTokenProvider.validateToken(token)) {
				return null;
			}
			Long userId = Long.valueOf(jwtTokenProvider.extractUid(token));
			setBookmarkStatusForPoses(userId);
		}

		return poseInfoRepository.findPoses(pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

	@Transactional(readOnly = true)
	public PoseFeedResponse getPoseFeed(String accessToken, final PoseFeedRequest poseFeedRequest) throws
		IllegalAccessException {
		Long userId = 0L;
		if (null != accessToken) {
			logger.info("[getPoseFeed accessToken 존재-> 로그인 O]");
			String token = jwtTokenProvider.extractJwtToken(accessToken);
			if (!jwtTokenProvider.validateToken(token)) {
				return null;
			}
			userId = Long.valueOf(jwtTokenProvider.extractUid(token));
			//setBookmarkStatusForPoses(userId);
		}
		logger.info("[getPoseFeed accessToken 존재 X-> 로그인 X]");

		Pageable pageable = PageRequest.of(poseFeedRequest.getPageNumber(), poseFeedRequest.getPageSize());
		Slice<PoseInfoResponse> filteredContents;
		Slice<PoseInfo> slicedFilteredPoseInfo;
		List<PoseInfo> slicedFilteredResult;
		Slice<PoseInfoResponse> recommendedContents;
		Slice<PoseInfo> slicedRecommenededPoseInfo;
		List<PoseInfo> slicedRecommenededResult;

		if (poseFeedRequest.getTags() != null && poseFeedRequest.getTags().equals("")) {
			poseFeedRequest.setTags(null);
		}
		Boolean getRecommendationCheck = poseFilterRepository.getRecommendationCheck(poseFeedRequest.getPeopleCount(),
			poseFeedRequest.getFrameCount(), poseFeedRequest.getTags());

		if (poseFeedRequest.getPageNumber() == 0) {
			if (poseFeedRequest.getTags() == null) {
				logger.info("[태그 요청] tags is NULL");
				filteredPoseInfo = poseFilterRepository.findByFilterNoTag(pageable, poseFeedRequest.getPeopleCount(),
					poseFeedRequest.getFrameCount(), userId);
			} else {
				filteredPoseInfo = poseFilterRepository.findByFilter(pageable, poseFeedRequest.getPeopleCount(),
					poseFeedRequest.getFrameCount(), poseFeedRequest.getTags(), userId);
			}
		}

		Integer endIdx = Math.min(filteredPoseInfo.size(), (int)pageable.getOffset() + pageable.getPageSize());

		if ((int)pageable.getOffset() >= endIdx) {
			slicedFilteredResult = new ArrayList<>();
		} else {
			slicedFilteredResult = filteredPoseInfo.subList((int)pageable.getOffset(), endIdx);
		}

		slicedFilteredPoseInfo = new SliceImpl<>(slicedFilteredResult, pageable,
			slicedFilteredResult.size() == pageable.getPageSize());
		filteredContents = slicedFilteredPoseInfo.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));

		if (getRecommendationCheck) {
			if (poseFeedRequest.getPageNumber() == 0) {
				recommendedPoseInfo = poseFilterRepository.getRecommendedContents(pageable, userId);
			}

			endIdx = Math.min(recommendedPoseInfo.size(), (int)pageable.getOffset() + pageable.getPageSize());

			if ((int)pageable.getOffset() >= endIdx) {
				slicedRecommenededResult = new ArrayList<>();
			} else {
				slicedRecommenededResult = recommendedPoseInfo.subList((int)pageable.getOffset(), endIdx);
			}

			slicedRecommenededPoseInfo = new SliceImpl<>(slicedRecommenededResult, pageable,
				slicedRecommenededResult.size() == pageable.getPageSize());
			recommendedContents = slicedRecommenededPoseInfo.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
			return new PoseFeedResponse(filteredContents, recommendedContents);
		}

		return new PoseFeedResponse(filteredContents);

	}

}
