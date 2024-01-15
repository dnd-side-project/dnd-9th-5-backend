package com.dndoz.PosePicker.Domain;

import javax.persistence.*;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApiModel(value = "포즈 태그 모델: PoseTag")
@AllArgsConstructor
@Entity(name = "tag")
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PoseTag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;

	@ManyToOne
	@JoinColumn(name = "attribute_id")
	private PoseTagAttribute poseTagAttribute;

	@ManyToOne
	@JoinColumn(name = "pose_id")
	private PoseInfo poseInfo;

	public PoseTag(PoseTagAttribute poseTagAttribute, PoseInfo poseInfo) {
		this.poseTagAttribute = poseTagAttribute;
		this.poseInfo = poseInfo;
	}

	public Long getTagId() {
		return tagId;
	}

	public PoseTagAttribute getPoseTagAttribute() {
		return poseTagAttribute;
	}

	public PoseInfo getPoseInfo() {
		return poseInfo;
	}
}

