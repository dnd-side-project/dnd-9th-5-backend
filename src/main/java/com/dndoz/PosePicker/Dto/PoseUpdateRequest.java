package com.dndoz.PosePicker.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PoseUpdateRequest {
	private Long poseId;
	private String peopleCount;
	private String frameCount;
	private String imageKey;
	private String tags;
	private String source;
	private String sourceUrl;
	private String description;

	public PoseUpdateRequest(Long poseId, String peopleCount, String frameCount, String imageKey, String tags,
		String source, String sourceUrl, String description) {
		this.poseId = poseId;
		this.peopleCount = peopleCount;
		this.frameCount = frameCount;
		this.imageKey = imageKey;
		this.tags = tags;
		this.source = source;
		this.sourceUrl = sourceUrl;
		this.description = description;
	}

}
