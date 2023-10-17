package com.dndoz.PosePicker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dndoz.PosePicker.Domain.PoseInfo;

@Repository
public interface PoseInfoRepository extends JpaRepository<PoseInfo, Long> {
	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (p.pose_id = :pose_id) " + "GROUP BY p.pose_id", nativeQuery = true)
	Optional<PoseInfo> findByPoseId(Long pose_id);

	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (:people_count < 5 AND p.people_count = :people_count) "
			+ "OR (:people_count >= 5 AND p.people_count >= 5) " + "GROUP BY p.pose_id "
			+ "ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Optional<PoseInfo> findRandomPoseInfo(Long people_count);

	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "GROUP BY p.pose_id ", nativeQuery = true)
	Slice<PoseInfo> findPoses(Pageable pageable);

	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE p.people_count = :peopleCount AND p.frame_count = :frameCount AND ta.attribute IN (:tags)"
			+ "GROUP BY p.pose_id ", nativeQuery = true)
	Slice<PoseInfo> findByFilter(Pageable pageable, Long peopleCount, Long frameCount, List<String> tags);

	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "GROUP BY p.pose_id " + "ORDER BY  RAND() ", nativeQuery = true)
	Slice<PoseInfo> getRecommendedContents(Pageable pageable);


	//사용자 북마크 보관 리스트
	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS t_tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "JOIN bookmark b ON b.pose_id = p.pose_id "
			+ "WHERE b.uid = :uid "
			+ "GROUP BY p.pose_id ", nativeQuery = true)
	Slice<PoseInfo> findBookmark(@Param("uid") String uid, Pageable pageable);

}
