package com.dndoz.PosePicker.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Domain.PoseTalk;
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
	@Value("${aws.imageUrl.prefix}")
	private String urlPrefix;

	public PoseService(final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository,
		final PoseTagAttributeRepository poseTagAttributeRepository) {
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
	}

	//포즈 이미지 상세 조회
	public PoseInfoResponse getPoseInfo(Long pose_id) {
		PoseInfo poseInfo = poseInfoRepository.findById(pose_id).orElseThrow(NullPointerException::new);
		poseInfo.setImageKey(urlPrefix + poseInfo.getImageKey());
		return new PoseInfoResponse(poseInfo);
	}

	//포즈픽(사진) 조회
	public PoseInfoResponse showRandomPoseInfo(Integer people_count) {
		PoseInfo poseInfo = poseInfoRepository.findRandomPoseInfo(people_count).orElseThrow(NullPointerException::new);
		return new PoseInfoResponse(poseInfo);
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
}
