package com.dndoz.PosePicker.Dto;

import com.dndoz.PosePicker.Domain.PoseInfo;
import io.swagger.annotations.ApiModel;

@ApiModel(value="포즈 이미지 응답: PoseInfoResponse")
public class PoseInfoResponse {

    private PoseInfo poseinfo;
    private PoseInfoResponse(){

    }

    public PoseInfoResponse(final PoseInfo poseInfo) {
        this.poseinfo=poseInfo;
    }

    public PoseInfo getPoseInfo(){
        return poseinfo;
    }

}
