package com.dndoz.PosePicker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dndoz.PosePicker.Domain.PoseTalk;

public interface PoseTalkRepository extends JpaRepository<PoseTalk, Long> {

	@Query(value = "SELECT * FROM pose_word p ORDER BY RAND() LIMIT 1 ", nativeQuery = true)
	PoseTalk findRandomPoseTalk();
}
