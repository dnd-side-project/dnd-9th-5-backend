package com.dndoz.PosePicker.Controller;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pose")
public class PoseInfoController {

    @Autowired
    private PoseInfoRepository poseInfoRepository;

    //전체 포즈 조회
    @GetMapping("/")
    public List<PoseInfo> showAllPoseInfo() {
        return poseInfoRepository.findAll();
    }

    //상세 포즈 조회
    @GetMapping("/{pose_id}")
    public PoseInfo showDetailPoseInfo(@PathVariable int pose_id) {
        Optional<PoseInfo> poseInfo = poseInfoRepository.findById(pose_id);

        if(!poseInfo.isPresent()){
            return poseInfo.orElse(null);
        }
        return poseInfo.get();
    }

}
