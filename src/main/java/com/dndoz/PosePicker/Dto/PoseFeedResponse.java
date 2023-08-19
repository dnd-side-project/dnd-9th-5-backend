package com.dndoz.PosePicker.Dto;

import org.springframework.data.domain.Slice;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "포즈 피드 리스트 응답: PoseFeedResponse")
public class PoseFeedResponse {
	private Boolean recommendation = true;
	private Slice<PoseInfoResponse> filteredContents;
	private Slice<PoseInfoResponse> recommendedContents;

	private PoseFeedResponse() {
	}

	public PoseFeedResponse(final Slice<PoseInfoResponse> filteredContents) {
		this.recommendation = false;
		this.filteredContents = filteredContents;
	}

	public PoseFeedResponse(final Slice<PoseInfoResponse> filteredContents,
		final Slice<PoseInfoResponse> recommendedContents) {
		this.recommendation = true;
		this.filteredContents = filteredContents;
		this.recommendedContents = recommendedContents;
	}

	public Boolean getRecommendation() {
		return recommendation;
	}

	public Slice<PoseInfoResponse> getFilteredContents() {
		return filteredContents;
	}

	public Slice<PoseInfoResponse> getRecommendedContents() {
		return recommendedContents;
	}
}


