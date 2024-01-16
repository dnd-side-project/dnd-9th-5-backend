package com.dndoz.PosePicker.Repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dndoz.PosePicker.Domain.Bookmark;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.User;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
	//북마크 여부 확인
	Optional<Boolean> findByUserAndPoseInfo(User user, PoseInfo poseInfo);

	//북마크 여부 확인
	@Query(value="SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END as bookmarkCheck FROM bookmark b WHERE b.uid = :userId AND b.pose_id = :poseId", nativeQuery = true)
	BigInteger existsByUserIdAndPoseId(@Param("userId") Long userId, @Param("poseId") Long poseId);

	//북마크 삭제
	void deleteByUserAndPoseInfo(User user, PoseInfo poseInfo);

	//회원탈퇴 시, 북마크 정보 삭제
	void deleteByUser(User user);
}
