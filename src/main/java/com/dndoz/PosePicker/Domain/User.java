package com.dndoz.PosePicker.Domain;

import com.dndoz.PosePicker.Global.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public User(Long uid, String nickname, String email){
        this.uid=uid;
        this.nickname=nickname;
        this.email=email;
    }

}
