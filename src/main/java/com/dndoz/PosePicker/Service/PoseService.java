package com.dndoz.PosePicker.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Domain.PoseTalk;
import com.dndoz.PosePicker.Dto.PoseFeedRequest;
import com.dndoz.PosePicker.Dto.PoseFeedResponse;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTagAttributeResponse;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.PoseTagAttributeRepository;
import com.dndoz.PosePicker.Repository.PoseTalkRepository;

@Transactional(readOnly = true)
@Service
public class PoseService {
	private final PoseInfoRepository poseInfoRepository;
	private final PoseTalkRepository poseTalkRepository;
	private final PoseTagAttributeRepository poseTagAttributeRepository;

	@Value("${aws.image_url.prefix}")
	private String urlPrefix;

	public PoseService(final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository,
		final PoseTagAttributeRepository poseTagAttributeRepository) {
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
	}

	//포즈 이미지 상세 조회
	public PoseInfoResponse getPoseInfo(Long pose_id) {
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(pose_id).orElseThrow(NullPointerException::new);
		return new PoseInfoResponse(urlPrefix, poseInfo);
	}

	//포즈픽(사진) 조회
	public PoseInfoResponse showRandomPoseInfo(Long people_count) {
		PoseInfo poseInfo = poseInfoRepository.findRandomPoseInfo(people_count).orElseThrow(NullPointerException::new);
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

	@Transactional(readOnly = true)
	public Slice<PoseInfoResponse> findPoses(final Integer pageNumber, final Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return poseInfoRepository.findPoses(pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

	@Transactional(readOnly = true)
	public PoseFeedResponse getPoseFeed(final PoseFeedRequest poseFeedRequest) {
		Pageable pageable = PageRequest.of(poseFeedRequest.getPageNumber(), poseFeedRequest.getPageSize());
		Slice<PoseInfoResponse> filteredContents;
		Slice<PoseInfoResponse> recommendedContents;

		Integer filteredContentsCount = poseInfoRepository.findByFilteredContentsCount(poseFeedRequest.getPeopleCount(),
			poseFeedRequest.getFrameCount(), poseFeedRequest.getTags());

		filteredContents = poseInfoRepository.findByFilter(pageable, poseFeedRequest.getPeopleCount(),
				poseFeedRequest.getFrameCount(), poseFeedRequest.getTags())
			.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));

		if (filteredContentsCount < 5) {
			recommendedContents = poseInfoRepository.getRecommendedContents(pageable)
				.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
			return new PoseFeedResponse(filteredContents, recommendedContents);

		}
		return new PoseFeedResponse(filteredContents);

	}

}
