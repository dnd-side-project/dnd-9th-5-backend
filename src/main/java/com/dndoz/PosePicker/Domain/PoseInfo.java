package com.dndoz.PosePicker.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

	@Transient
	private String tagAttributes;

	public PoseInfo(PoseInfo poseInfo, String tagAttributes) {
		this.poseId = poseInfo.getPoseId();
		this.imageKey = poseInfo.getImageKey();
		this.source = poseInfo.getSource();
		this.sourceUrl = poseInfo.getSourceUrl();
		this.poseId = poseInfo.getPoseId();
		this.peopleCount = poseInfo.getPeopleCount();
		this.frameCount = poseInfo.getFrameCount();
		this.tagAttributes = tagAttributes;
	}
}

