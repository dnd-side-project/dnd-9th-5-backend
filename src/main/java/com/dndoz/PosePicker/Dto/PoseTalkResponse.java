package com.dndoz.PosePicker.Dto;
import com.dndoz.PosePicker.Domain.PoseWord;

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
