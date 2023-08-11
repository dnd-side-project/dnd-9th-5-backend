package com.dndoz.PosePicker.Controller;

import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Service.PoseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pose")
@Api(tags = {"포즈사진 API"})
public class PoseInfoController {

    private final PoseInfoService poseInfoService;

    public PoseInfoController(final PoseInfoService poseInfoService) {
        this.poseInfoService=poseInfoService;
    }

    //포즈 이미지 상세 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{pose_id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "포즈 사진 상세 조회 성공"),
            @ApiResponse(code=401, message="접근 권한이 없습니다.")
    })
    @ApiOperation(value = "포즈 사진 상세 조회", notes = "사진 클릭 시 포즈상세정보")
    public ResponseEntity<PoseInfoResponse> getPoseInfo(@PathVariable Long pose_id) {
        try{
            return ResponseEntity.ok(poseInfoService.getPoseInfo(pose_id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }

    }

}
