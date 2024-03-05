package com.dndoz.PosePicker.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "withdrawal")
public class Withdrawal extends BaseEntity {
	@Id
	@Column(name="withdrawalId")
	private Long withdrawalId;

	@Column(name = "uid")
	private Long uid;

	@Column(name = "reason")
	String reason;

	public Withdrawal(Long uid, String reason) {
		this.uid = uid;
		this.reason = reason;
	}
}
