package com.dndoz.PosePicker.Dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ImgUploadRequest {
	private String peopleCount;
	private String frameCount;
	private String tags;
	private String source;
	private String sourceUrl;
	private String description;

	public ImgUploadRequest(String peopleCount,String frameCount,String tags,
		String source, String sourceUrl, String description) {
		this.peopleCount= peopleCount;
		this.frameCount= frameCount;
		this.tags= tags;
		this.source= source;
		this.sourceUrl= sourceUrl;
		this.description= description;
	}

}
