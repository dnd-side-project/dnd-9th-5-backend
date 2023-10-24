package com.dndoz.PosePicker.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkRequest{

	private Long uid;
	private Long poseId;

	public BookmarkRequest(Long uid, Long poseId) {
		this.uid= uid;
		this.poseId= poseId;
	}
}
