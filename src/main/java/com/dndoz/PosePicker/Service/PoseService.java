package com.dndoz.PosePicker.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
import com.dndoz.PosePicker.Repository.PoseFilterRepository;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.PoseTagAttributeRepository;
import com.dndoz.PosePicker.Repository.PoseTalkRepository;

@Transactional(readOnly = true)
@Service
public class PoseService {
	private final PoseInfoRepository poseInfoRepository;
	private final PoseTalkRepository poseTalkRepository;
	private final PoseFilterRepository poseFilterRepository;
	private final PoseTagAttributeRepository poseTagAttributeRepository;

	@Value("${aws.image_url.prefix}")
	private String urlPrefix;

	private List<PoseInfo> filteredPoseInfo;
	private List<PoseInfo> recommendedPoseInfo;

	public PoseService(final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository,
		final PoseFilterRepository poseFilterRepository, final PoseTagAttributeRepository poseTagAttributeRepository) {
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseFilterRepository = poseFilterRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
	}

	//포즈 이미지 상세 조회
	public PoseInfoResponse getPoseInfo(Long pose_id) {
		PoseInfo poseInfo = poseFilterRepository.findByPoseId(pose_id).orElseThrow(NullPointerException::new);
		return new PoseInfoResponse(urlPrefix, poseInfo);
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

	@Transactional(readOnly = true)
	public Slice<PoseInfoResponse> findPoses(final Integer pageNumber, final Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return poseInfoRepository.findPoses(pageable).map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
	}

	@Transactional(readOnly = true)
	public PoseFeedResponse getPoseFeed(final PoseFeedRequest poseFeedRequest) {
		Pageable pageable = PageRequest.of(poseFeedRequest.getPageNumber(), poseFeedRequest.getPageSize());
		Slice<PoseInfoResponse> filteredContents;
		Slice<PoseInfo> slicedFilteredPoseInfo;
		List<PoseInfo> slicedFilteredResult;
		Slice<PoseInfoResponse> recommendedContents;
		Slice<PoseInfo> slicedRecommenededPoseInfo;
		List<PoseInfo> slicedRecommenededResult;

		Boolean getRecommendationCheck = poseFilterRepository.getRecommendationCheck(poseFeedRequest.getPeopleCount(),
			poseFeedRequest.getFrameCount(), poseFeedRequest.getTags());

		if (poseFeedRequest.getPageNumber() == 0) {
			if (null==poseFeedRequest.getTags()) {
				System.out.println("PoseSerivce: tags is NULL");
				filteredPoseInfo= poseFilterRepository.findByFilterNoTag(pageable, poseFeedRequest.getPeopleCount(),
					poseFeedRequest.getFrameCount());
			} else {
				filteredPoseInfo = poseFilterRepository.findByFilter(pageable, poseFeedRequest.getPeopleCount(),
					poseFeedRequest.getFrameCount(), poseFeedRequest.getTags());
			}

		}

		Integer endIdx = Math.min(filteredPoseInfo.size(), (int)pageable.getOffset() + pageable.getPageSize());

		if ((int)pageable.getOffset() >= endIdx) {
			slicedFilteredResult = new ArrayList<>();
		} else {
			slicedFilteredResult = filteredPoseInfo.subList((int)pageable.getOffset(), endIdx);
		}

		slicedFilteredPoseInfo =  new SliceImpl<>(slicedFilteredResult, pageable, slicedFilteredResult.size() == pageable.getPageSize());
		filteredContents = slicedFilteredPoseInfo.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));

		if (getRecommendationCheck) {
			if (poseFeedRequest.getPageNumber() == 0) {
				recommendedPoseInfo = poseFilterRepository.getRecommendedContents(pageable);
			}

			endIdx = Math.min(recommendedPoseInfo.size(), (int)pageable.getOffset() + pageable.getPageSize());

			if ((int)pageable.getOffset() >= endIdx) {
				slicedRecommenededResult = new ArrayList<>();
			} else {
				slicedRecommenededResult = recommendedPoseInfo.subList((int)pageable.getOffset(), endIdx);
			}

			slicedRecommenededPoseInfo = new SliceImpl<>(slicedRecommenededResult, pageable, slicedRecommenededResult.size() == pageable.getPageSize());
			recommendedContents = slicedRecommenededPoseInfo.map(poseInfo -> new PoseInfoResponse(urlPrefix, poseInfo));
			return new PoseFeedResponse(filteredContents, recommendedContents);
		}

		return new PoseFeedResponse(filteredContents);

	}

}
