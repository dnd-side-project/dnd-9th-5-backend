package com.dndoz.PosePicker.Service;

import com.dndoz.PosePicker.Repository.PoseRepository;
import com.dndoz.PosePicker.Domain.PoseWord;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PoseService {
    private final PoseRepository poseRepository;

    public PoseService(final PoseRepository poseRepository) {
        this.poseRepository = poseRepository;
    }

    public PoseTalkResponse findRandomPoseTalk() {
        PoseWord poseWord = poseRepository.findRandomPoseTalk();
        return new PoseTalkResponse(poseWord);
    }

}
