package com.dndoz.PosePicker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.dndoz.PosePicker.Domain.PoseInfo;

public interface PoseFilterRepositoryCustom {
	Optional<PoseInfo> findByPoseId(Long pose_id);

	Optional<PoseInfo> findRandomPoseInfo(Long people_count);

	Boolean getRecommendationCheck(Long people_count, Long frame_count, String tags);

	List<PoseInfo> findByFilter(Pageable pageable, Long people_count, Long frame_count, String tags, Long userId);

	List<PoseInfo> findByFilterNoTag(Pageable pageable, Long people_count, Long frame_count, Long userId);

	List<PoseInfo> getRecommendedContents(Pageable pageable, Long userId);

}
