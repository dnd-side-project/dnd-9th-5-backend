package com.dndoz.PosePicker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dndoz.PosePicker.Domain.Bookmark;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.User;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
	//북마크 여부 확인
	Optional<Boolean> findByUserAndPoseInfo(User user, PoseInfo poseInfo);
	//북마크 삭제
	void deleteByUserAndPoseInfo(User user, PoseInfo poseInfo);
}
