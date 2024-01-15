package com.dndoz.PosePicker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dndoz.PosePicker.Domain.PoseTag;

@Repository
public interface PoseTagRepository extends JpaRepository<PoseTag, Long> {

	@Modifying
	@Query(value = "DELETE FROM tag WHERE pose_id = :pose_id", nativeQuery = true)
	void deleteByPoseId(Long pose_id);
}
