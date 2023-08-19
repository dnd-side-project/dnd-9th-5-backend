package com.dndoz.PosePicker.Dto;

import com.dndoz.PosePicker.Domain.PoseInfo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "포즈 이미지 응답: PoseInfoResponse")
public class PoseInfoResponse {

	private PoseInfo poseInfo;

	private PoseInfoResponse() {

	}

	public PoseInfoResponse(final String urlPrefix, final PoseInfo poseInfo) {
		poseInfo.setImageKey(urlPrefix + poseInfo.getImageKey());

		this.poseInfo = poseInfo;
	}

	public PoseInfo getPoseInfo() {
		return poseInfo;
	}

}
