package com.dndoz.PosePicker.Repository;

import com.dndoz.PosePicker.Domain.PoseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PoseInfoRepository extends JpaRepository<PoseInfo,Long> {

}
