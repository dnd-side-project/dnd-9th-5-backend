package com.dndoz.PosePicker.Repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.dndoz.PosePicker.Domain.PoseInfo;

public interface PoseFilterRepositoryCustom {

	Optional<PoseInfo> findByPoseId(Long pose_id);

	Optional<PoseInfo> findRandomPoseInfo(Long people_count);

	Boolean getRecommendationCheck(Long people_count, Long frame_count, String tags);

	Slice<PoseInfo> findByFilter(Pageable pageable, Long people_count, Long frame_count, String tags);

	Slice<PoseInfo> getRecommendedContents(Pageable pageable);

}
