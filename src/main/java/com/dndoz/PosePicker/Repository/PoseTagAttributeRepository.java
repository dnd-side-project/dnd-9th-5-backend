package com.dndoz.PosePicker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dndoz.PosePicker.Domain.PoseTagAttribute;

public interface PoseTagAttributeRepository extends JpaRepository<PoseTagAttribute, Long> {

	@Query(value = "SELECT * FROM tag_attribute", nativeQuery = true)
	List<PoseTagAttribute> findPoseTagAttribute();
}
