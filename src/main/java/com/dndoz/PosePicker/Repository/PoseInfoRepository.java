package com.dndoz.PosePicker.Repository;

import com.dndoz.PosePicker.Domain.PoseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PoseInfoRepository extends JpaRepository<PoseInfo,Long> {
    @Query(value = "SELECT * FROM pose_info WHERE (:people_count < 5 AND people_count = :people_count) OR (:people_count >= 5 AND people_count >= 5) ORDER BY RAND() LIMIT 1", nativeQuery = true)
    PoseInfo findRandomPoseInfo(Integer people_count);
}



