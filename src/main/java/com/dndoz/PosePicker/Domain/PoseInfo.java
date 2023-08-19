package com.dndoz.PosePicker.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "pose_info")
@Table(name = "pose_info")
@ApiModel(value = "포즈 이미지 모델: PoseInfo")
public class PoseInfo extends BaseEntity {
	@Column(name = "image_key")
	String imageKey;
	@Column(name = "source")
	String source;
	@Column(name = "source_url")
	String sourceUrl;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long poseId;
	@Column(name = "people_count")
	private Long peopleCount;

	@Column(name = "frame_count")
	private Long frameCount;

	@JoinColumn(name = "tag_attributes")
	private String tagAttributes;

	public Long getPoseId() {
		return poseId;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getSource() {
		return source;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public Long getPeopleCount() {
		return peopleCount;
	}

	public Long getFrameCount() {
		return frameCount;
	}

	public String getTagAttributes() {
		return tagAttributes;
	}
}

