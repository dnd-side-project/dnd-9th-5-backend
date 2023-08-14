package com.dndoz.PosePicker.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApiModel(value = "포즈 단어 모델: PoseTalk")
@AllArgsConstructor
@Entity(name = "pose_word")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pose_word")
public class PoseTalk extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "word_id")
	private Long wordId;

	@Column(name = "content", nullable = false)
	private String content;

	public Long getWordId() {
		return wordId;
	}

	public String getContent() {
		return content;
	}
}
