package com.dndoz.PosePicker.Controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import com.dndoz.PosePicker.Service.PoseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/pose")
@Api(tags = {"포즈 API"})
public class PoseController {

	private final PoseService poseService;

	public PoseController(final PoseService poseService) {
		this.poseService = poseService;
	}

	/**
	 * @Description 포즈 이미지 상세 조회
	 * @param pose_id
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{pose_id}")
	@ApiResponses({@ApiResponse(code = 200, message = "포즈 사진 상세 조회 성공"),
		@ApiResponse(code = 401, message = "접근 권한이 없습니다.")})
	@ApiOperation(value = "포즈 사진 상세 조회", notes = "사진 클릭 시 포즈 상세 정보")
	public ResponseEntity<PoseInfoResponse> getPoseInfo(@PathVariable Long pose_id) {
		try {
			return ResponseEntity.ok(poseService.getPoseInfo(pose_id));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
		}

	}

	/**
	 * @Description 포즈픽 사진 조회
	 * @param people_count
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/pick/{people_count}")
	@ApiResponse(code = 200, message = "포즈픽 데이터 전달 성공")
	@ApiOperation(value = "포즈픽", notes = "포즈사진 랜덤픽")
	public ResponseEntity<PoseInfoResponse> showRandomPoseInfo(@PathVariable int people_count) {
		try {
			return ResponseEntity.ok(poseService.showRandomPoseInfo(people_count));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
		}
	}

	/**
	 * @Description 포즈톡 단어 조회
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/talk")
	@ApiResponse(code = 200, message = "포즈톡 데이터 전달 성공")
	@ApiOperation(value = "포즈톡", notes = "포즈단어 랜덤톡")
	public ResponseEntity<PoseTalkResponse> findRandomPoseTalk() {
		return ResponseEntity.ok(poseService.findRandomPoseTalk());
	}

}
