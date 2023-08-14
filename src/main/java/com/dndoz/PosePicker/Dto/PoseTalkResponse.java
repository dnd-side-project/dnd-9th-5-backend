package com.dndoz.PosePicker.Dto;

import com.dndoz.PosePicker.Domain.PoseTalk;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "포즈톡 응답: PoseTalkResponse")
public class PoseTalkResponse {

	private PoseTalk poseWord;

	private PoseTalkResponse() {
	}

	public PoseTalkResponse(final PoseTalk poseWord) {
		this.poseWord = poseWord;
	}

	public PoseTalk getPoseWord() {
		return poseWord;
	}
}
