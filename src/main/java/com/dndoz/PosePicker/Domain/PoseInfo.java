package com.dndoz.PosePicker.Domain;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="pose_info")
@Getter @Setter

@ApiModel(value="포즈 이미지 모델: PoseInfo")
public class PoseInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pose_id;

    @Column(name = "image_key")
    String image_key;

    @Column(name = "source")
    String pose_source;

    @Column(name = "people_count")
    private Integer people_count;

    @Column(name = "frame_count")
    private Integer frame_count;

}
