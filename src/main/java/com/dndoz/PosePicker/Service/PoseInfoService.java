package com.dndoz.PosePicker.Service;

import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PoseInfoService {

    private final PoseInfoRepository poseInfoRepository;

    //포즈 이미지 상세 조회
    public PoseInfoResponse getPoseInfo(Long pose_id){
        Optional<PoseInfo> optionalPoseInfo=poseInfoRepository.findById(pose_id);
        if (optionalPoseInfo.isPresent()){
            return new PoseInfoResponse(optionalPoseInfo.get());
        }else{
            return null;
        }
    }
}
