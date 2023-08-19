package com.dndoz.PosePicker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dndoz.PosePicker.Domain.PoseInfo;

@Repository
public interface PoseInfoRepository extends JpaRepository<PoseInfo, Long> {
	@Query(value =
		"SELECT p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at,"
			+ "GROUP_CONCAT(ta.attribute) AS tag_attributes " + "FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (p.pose_id = :pose_id) "
			+ "GROUP BY p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at ", nativeQuery = true)
	Optional<PoseInfo> findByPoseId(Long pose_id);

	@Query(value =
		"SELECT p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at,"
			+ "GROUP_CONCAT(ta.attribute) AS tag_attributes " + "FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (:people_count < 5 AND p.people_count = :people_count) "
			+ "OR (:people_count >= 5 AND p.people_count >= 5) "
			+ "GROUP BY p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at "
			+ "ORDER BY RAND() " + "LIMIT 1", nativeQuery = true)
	Optional<PoseInfo> findRandomPoseInfo(Long people_count);

	// 	@Query(value = "SELECT * FROM tag_attribute", nativeQuery = true)
	// 	List<PoseInfo> findByFilter(Pose poseCondition, Pageable pageable);
	// }

	@Query(value =
		"SELECT p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at,"
			+ "GROUP_CONCAT(ta.attribute) AS tag_attributes " + "FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "GROUP BY p.pose_id, p.image_key, p.source, p.source_url, p.people_count, p.frame_count, p.created_at, p.updated_at", nativeQuery = true)
	Page<PoseInfo> findPoses(Pageable pageable);

	// frameCount None
	@Query("SELECT p FROM pose_info p JOIN tag t ON p.poseId = t.poseInfo.poseId WHERE p.peopleCount = :peopleCount AND t.poseTagAttribute.attributeId IN :tags")
	Page<PoseInfo> findPosesByPeopleCount(@Param("peopleCount") Long peopleCount, @Param("tags") List<Long> tags,
		Pageable pageable);

	// peopleCount None
	@Query("SELECT p FROM pose_info p JOIN tag t ON p.poseId = t.poseInfo.poseId WHERE p.frameCount = :frameCount AND t.poseTagAttribute.attributeId IN :tags")
	Page<PoseInfo> findPosesByFrameCount(@Param("frameCount") Long frameCount, @Param("tags") List<Long> tags,
		Pageable pageable);

	// tags
	@Query("SELECT p FROM pose_info p JOIN tag t ON p.poseId = t.poseInfo.poseId AND t.poseTagAttribute.attributeId IN :tags")
	Page<PoseInfo> findPosesByTags(@Param("tags") List<Long> tags, Pageable pageable);

	@Query("SELECT p FROM pose_info p JOIN tag t ON p.poseId = t.poseInfo.poseId WHERE p.peopleCount = :peopleCount AND p.frameCount = :frameCount AND t.poseTagAttribute.attributeId IN :tags")
	Page<PoseInfo> findPosesByCriteria(@Param("peopleCount") Long peopleCount, @Param("frameCount") Long frameCount,
		@Param("tags") List<Long> tags, Pageable pageable);
}
