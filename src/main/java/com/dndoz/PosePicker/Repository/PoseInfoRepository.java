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

	//포즈피드 북마크 여부 확인
	@Query(value="SELECT p.pose_id, CASE WHEN b.uid IS NOT NULL THEN TRUE ELSE FALSE END AS bookmarkCheck " +
		"FROM pose_info p LEFT JOIN bookmark b ON p.pose_id = b.pose_id AND b.uid = :userId", nativeQuery = true)
	List<Object[]> findBookmarkStatusByUserId(@Param("userId") Long userId);

	@Query(value="SELECT p.* FROM pose_info p LEFT JOIN bookmark b ON p.pose_id = b.pose_id AND b.uid = :userId", nativeQuery = true)
	List<PoseInfo> findPosesWithBookmarkStatus(@Param("userId") Long userId);

	@Query(value =
		"SELECT COUNT(*) "
			+ "FROM (SELECT p.pose_id FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (:people_count = 0 OR p.people_count = :people_count) AND (:frame_count = 0 OR p.frame_count = :frame_count) AND (:tags = IS NULL OR FIND_IN_SET(ta.attribute, :tags) > 0) "
			+ "GROUP BY p.pose_id ) AS sub_query ", nativeQuery = true)
	Integer findByFilteredContentsCount(Long people_count, Long frame_count, String tags);

	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (:people_count = 0 OR p.people_count = :people_count) AND (:frame_count = 0 OR p.frame_count = :frame_count) AND (:tags IS NULL OR FIND_IN_SET(ta.attribute, :tags) > 0) "
			+ "GROUP BY p.pose_id ", nativeQuery = true)
	Slice<PoseInfo> findByFilter(Pageable pageable, Long people_count, Long frame_count, String tags);

	//태그 정보 없이 요청보낼 때
	@Query(value =
		"SELECT p.*, GROUP_CONCAT(ta.attribute ORDER BY ta.attribute_id ASC) AS tag_attributes FROM pose_info p "
			+ "JOIN tag t ON p.pose_id = t.pose_id " + "JOIN tag_attribute ta ON t.attribute_id = ta.attribute_id "
			+ "WHERE (:people_count = 0 OR p.people_count = :people_count) AND (:frame_count = 0 OR p.frame_count = :frame_count) "
			+ "GROUP BY p.pose_id " + "ORDER BY RAND()", nativeQuery = true)
	Slice<PoseInfo> findByFilterNoTag(Pageable pageable, Long people_count, Long frame_count);

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
	Slice<PoseInfo> findBookmark(@Param("uid") Long uid, Pageable pageable);

}
