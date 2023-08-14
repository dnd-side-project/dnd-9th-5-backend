package com.dndoz.PosePicker.Dto;

import java.util.List;

import com.dndoz.PosePicker.Domain.PoseTagAttribute;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "포즈 태그 속성 응답: PoseTagAttributeResponse")
public class PoseTagAttributeResponse {

	private List<PoseTagAttribute> poseTagAttributes;

	private PoseTagAttributeResponse() {
	}

	public PoseTagAttributeResponse(final List<PoseTagAttribute> poseTagAttributes) {
		this.poseTagAttributes = poseTagAttributes;
	}

	public List<PoseTagAttribute> getPoseTagAttributes() {
		return poseTagAttributes;
	}
}
