package com.dndoz.PosePicker.Domain;

import javax.persistence.*;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

@Entity(name = "tag")
@Table(name = "tag")
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

