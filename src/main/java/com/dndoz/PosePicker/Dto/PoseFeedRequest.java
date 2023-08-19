package com.dndoz.PosePicker.Dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "포즈 피드 요청: PoseConditionRequest")
public class PoseFeedRequest {

	@ApiModelProperty(name = "peopleCount", dataType = "Long", example = "4", value = "0")
	private Long peopleCount = 0L;
	@ApiModelProperty(name = "frameCount", dataType = "Long", example = "4", value = "0")
	private Long frameCount = 0L;
	@ApiModelProperty(name = "tags", dataType = "List<Long>", value = "", example = "[\"10\", \"11\", \"12\"]")
	private List<Long> tags;

	public PoseFeedRequest() {

	}

	public PoseFeedRequest(final Long peopleCount, final Long frameCount, final List<Long> tags) {
		this.peopleCount = peopleCount;
		this.frameCount = frameCount;
		this.tags = tags;
	}

	public Long getPeopleCount() {
		return peopleCount;
	}

	public Long getFrameCount() {
		return frameCount;
	}

	public List<Long> getTags() {
		return tags;
	}
}
