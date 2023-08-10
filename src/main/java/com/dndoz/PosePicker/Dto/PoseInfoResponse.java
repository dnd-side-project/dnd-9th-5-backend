package com.dndoz.PosePicker.Dto;

import com.dndoz.PosePicker.Domain.PoseInfo;


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
