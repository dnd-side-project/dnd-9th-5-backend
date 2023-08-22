package com.dndoz.PosePicker.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bookmark")
public class Bookmark{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookmark_id")
	private Long bookmarkId;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "uid")
	private User user;

	@ManyToOne(targetEntity = PoseInfo.class)
	@JoinColumn(name = "pose_id")
	private PoseInfo poseInfo;

	public Bookmark(User user, PoseInfo poseInfo) {
		this.user = user;
		this.poseInfo = poseInfo;
	}

}
