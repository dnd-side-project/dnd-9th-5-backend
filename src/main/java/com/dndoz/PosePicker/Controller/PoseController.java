package com.dndoz.PosePicker.Controller;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTagAttributeResponse;
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
	public ResponseEntity<PoseInfoResponse> showRandomPoseInfo(@PathVariable Long people_count) {
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

	/**
	 * @Description 포즈 태그 리스트 조회
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/tags")
	@ApiResponse(code = 200, message = "포즈 태그 리스트 전달 성공")
	@ApiOperation(value = "포즈 태그", notes = "포즈 태그 리스트")
	public ResponseEntity<PoseTagAttributeResponse> findPoseTagAttribute() {
		return ResponseEntity.ok(poseService.findPoseTagAttribute());
	}

	// @ResponseStatus(HttpStatus.OK)
	// @GetMapping
	// @ApiResponse(code = 200, message = "전체 포즈 피드 리스트 조회 성공")
	// @ApiOperation(value = "전체 포즈 피드 리스트", notes = "전체 포즈 피드 리스트")
	// public ResponseEntity<PoseFeedResponse> findByFilter(
	// 	@PoseFilter PoseCondition poseCondition,
	// 	@RequestParam(defaultValue = "0") int page,
	// 	@RequestParam(defaultValue = "20") int size
	// 	) {
	// 	Pageable pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
	// 	return ResponseEntity.ok(poseService.findByFilter(poseCondition, pageRequest));
	// }
	//
	// @GetMapping
	// public ResponseEntity<MemberTicketsResponse> findAll(
	// 	@Login LoginMember loginMember,
	// 	@RequestParam(defaultValue = "0") int page,
	// 	@RequestParam(defaultValue = "100") int size) {
	// 	Pageable pageRequest = PageRequest.of(page, size, Sort.by("entryTime").descending());
	// 	MemberTicketsResponse response = memberTicketService.findAll(loginMember.memberId(), pageRequest);
	// 	return ResponseEntity.ok()
	// 		.body(response);
	// }
	// ... 기존의 메소드 및 필드들

	/**
	 * @Description 포즈 피드 전체 조회
	 * @return
	 */
	@GetMapping()
	@ApiResponse(code = 200, message = "포즈 피드 리스트 전달 성공")
	@ApiOperation(value = "포즈 피드", notes = "포즈 피드 리스트")
	public ResponseEntity<Page<PoseInfoResponse>> getPoses(
		@PageableDefault(size = 20) final Pageable pageable) {

		return ResponseEntity.ok(poseService.findPoses(pageable));
	}

	// @GetMapping()
	// @ApiResponse(code = 200, message = "포즈 피드 리스트 전달 성공")
	// @ApiOperation(value = "포즈 피드", notes = "포즈 피드 리스트")
	// public ResponseEntity<Page<PoseInfoResponse>> getPosesByCriteria(
	// 	final PoseFeedRequest poseFeedRequest,
	// 	@PageableDefault(size = 20) final Pageable pageable) {
	//
	// 	return ResponseEntity.ok(poseService.findPosesByCriteria(poseFeedRequest, pageable));
	// }

}
