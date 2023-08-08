package com.dndoz.PosePicker.controller;

import com.dndoz.PosePicker.dto.PoseTalkResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dndoz.PosePicker.service.PoseService;

@RestController
@RequestMapping("/api/pose")
@Api(tags = {"포즈 API"})
public class PoseController {

    private final PoseService poseService;

    public PoseController(final PoseService poseService) {
        this.poseService = poseService;
    }

    @GetMapping("/talk")
    @ApiResponse(code = 200, message = "Pose Word Data")
    @ApiOperation(value = "포즈 단어 랜덤 조회", notes="랜덤으로 포즈 단어 하나를 보여줍니다.")
    public ResponseEntity<PoseTalkResponse> findRandomPoseTalk() {
        return ResponseEntity.ok(poseService.findRandomPoseTalk());
    }
}