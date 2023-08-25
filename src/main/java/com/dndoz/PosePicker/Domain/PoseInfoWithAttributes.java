package com.dndoz.PosePicker.Domain;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "태그 포함 포즈 정보 모델: PoseInfoWithAttributes")
public class PoseInfoWithAttributes {
	private PoseInfo poseinfo;
	private String tagAttributes;

	public PoseInfo getPoseinfo() {
		return poseinfo;
	}

	public String getTagAttributes() {
		return tagAttributes;
	}
}
