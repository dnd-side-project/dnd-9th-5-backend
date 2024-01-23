package com.dndoz.PosePicker.Dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "포즈 피드 리스트 응답: PoseFeedRequest")
public class PoseFeedRequest {
	private Integer pageNumber = 0;
	private Integer pageSize = 8;
	private Long peopleCount = 0L;
	private Long frameCount = 0L;
	private String tags;

	private PoseFeedRequest() {
	}

	public PoseFeedRequest(final Integer pageNumber, final Long peopleCount,
		final Long frameCount, final String tags) {
		this.pageNumber = pageNumber != null ? pageNumber : this.pageNumber;
		this.peopleCount = peopleCount != null ? peopleCount : this.peopleCount;
		this.frameCount = frameCount != null ? frameCount : this.frameCount;
		this.tags = tags;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Long getPeopleCount() {
		return peopleCount;
	}

	public Long getFrameCount() {
		return frameCount;
	}

	public String getTags() {
		return tags;
	}
}


