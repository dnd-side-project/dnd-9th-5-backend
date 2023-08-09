package com.dndoz.PosePicker.Repository;

import com.dndoz.PosePicker.Domain.PoseWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PoseRepository extends JpaRepository<PoseWord, Long> {

    @Query(value = "SELECT * FROM pose_word p ORDER BY RAND() LIMIT 1 ", nativeQuery = true)
    PoseWord findRandomPoseTalk();
}