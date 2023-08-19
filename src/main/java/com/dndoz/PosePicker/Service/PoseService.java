package com.dndoz.PosePicker.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Domain.PoseTalk;
import com.dndoz.PosePicker.Dto.PoseFeedRequest;
// import com.dndoz.PosePicker.Dto.PoseFeedResponse;
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

	public PoseService(final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository, final PoseTagAttributeRepository poseTagAttributeRepository) {
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

	// @Transactional(readOnly = true)
	// public PoseFeedResponse findByFilter(PoseCondition poseCondition, Pageable pageable) {
	// 	List<PoseInfo> memberTickets = poseInfoRepository.findByFilter(poseCondition, pageable);
	// 	return memberTickets.stream()
	// 		.collect(collectingAndThen(toList(), MemberTicketsResponse::from));
	// }

	@Transactional(readOnly = true)
	public Page<PoseInfoResponse> findPoses(final Pageable pageable) {
		return poseInfoRepository.findPoses(pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

	// ... 기존의 메소드 및 필드들

	@Transactional(readOnly = true)
	public Page<PoseInfoResponse> findPosesByCriteria(final PoseFeedRequest poseFeedRequest, final Pageable pageable) {
		Page<PoseInfo>  Response;
		Long peopleCount = poseFeedRequest.getPeopleCount();
		Long frameCount = poseFeedRequest.getFrameCount();
		List<Long> tags = poseFeedRequest.getTags();

		System.out.println(peopleCount + frameCount + tags.get(0));

		if (peopleCount == 0 && frameCount == 0)
			Response = poseInfoRepository.findPosesByTags(tags, pageable);
		else if (peopleCount == 0)
			Response = poseInfoRepository.findPosesByFrameCount(frameCount, tags, pageable);
		else if (frameCount == 0)
			Response = poseInfoRepository.findPosesByPeopleCount(peopleCount, tags, pageable);
		else if (tags.isEmpty())
			Response = poseInfoRepository.findPoses(pageable);
		else
			Response = poseInfoRepository.findPosesByCriteria(peopleCount, frameCount, tags, pageable);

		return Response.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}
}
