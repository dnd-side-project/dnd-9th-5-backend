package com.dndoz.PosePicker.dto;
import com.dndoz.PosePicker.domain.PoseWord;

public class PoseTalkResponse {
    private PoseWord poseWord;

    private PoseTalkResponse() {
    }
    public PoseTalkResponse(final PoseWord poseWord) {
        this.poseWord = poseWord;
    }

    public PoseWord getPoseWord() {
        return poseWord;
    }
}
