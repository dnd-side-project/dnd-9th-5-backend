package com.dndoz.PosePicker.Domain;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Entity(name="user")
@Getter
@Setter
@ApiModel(value="사용자 모델: User")
public class User {
    @Id
    @Column(name="uid")
    private Long uid;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "email")
    String email;

	@Column(name = "loginType")
	String loginType;

	@Column(name = "iosId")
	String iosId;
}
