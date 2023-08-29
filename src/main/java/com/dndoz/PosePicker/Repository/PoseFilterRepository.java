package com.dndoz.PosePicker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dndoz.PosePicker.Domain.PoseInfo;

public interface PoseFilterRepository extends JpaRepository<PoseInfo, Long>, PoseFilterRepositoryCustom {

}
