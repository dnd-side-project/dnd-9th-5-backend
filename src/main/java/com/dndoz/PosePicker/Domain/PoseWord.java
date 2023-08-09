package com.dndoz.PosePicker.Domain;

import com.dndoz.PosePicker.Global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pose_word")
@Entity
public class PoseWord extends BaseEntity {
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