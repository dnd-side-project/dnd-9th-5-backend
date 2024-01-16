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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "포즈 태그 속성 모델: PoseTagAttribute")
@AllArgsConstructor
@Entity(name = "tag_attribute")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "tag_attribute")
@Getter
@Setter
public class PoseTagAttribute extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attribute_id")
	private Long attributeId;

	@Column(name = "attribute", nullable = false)
	private String attribute;

	public Long getAttributeId() {
		return attributeId;
	}

	public String getAttribute() {
		return attribute;
	}
}
