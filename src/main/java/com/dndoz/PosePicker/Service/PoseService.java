package com.dndoz.PosePicker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Domain.PoseTalk;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTagAttributeResponse;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.PoseTalkRepository;
import com.dndoz.PosePicker.Repository.PoseTagAttributeRepository;

@Transactional(readOnly = true)
@Service
public class PoseService {
	private final PoseInfoRepository poseInfoRepository;
	private final PoseTalkRepository poseTalkRepository;
	private final PoseTagAttributeRepository poseTagAttributeRepository;

	public PoseService(final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository,
		final PoseTagAttributeRepository poseTagAttributeRepository) {
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
	}

	//포즈 이미지 상세 조회
	public PoseInfoResponse getPoseInfo(Long pose_id) {
		Optional<PoseInfo> optionalPoseInfo = poseInfoRepository.findById(pose_id);
		if (optionalPoseInfo.isPresent()) {
			return new PoseInfoResponse(optionalPoseInfo.get());
		} else {
			return null;
		}
	}

	//포즈픽(사진) 조회
	public PoseInfoResponse showRandomPoseInfo(Integer people_count) {
		PoseInfo poseInfo = poseInfoRepository.findRandomPoseInfo(people_count);
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
