package com.dndoz.PosePicker.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dndoz.PosePicker.Dto.BookmarkRequest;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Service.BookmarkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
@Api(tags = {"북마크 API"})
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/")
	@ApiResponse(code = 201, message = "북마크 등록 성공")
	@ApiOperation(value = "북마크", notes = "북마크 등록")
	public ResponseEntity<?> insert(@RequestBody BookmarkRequest bookmarkDto) throws Exception {
		bookmarkService.insert(bookmarkDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/")
	@ApiResponse(code = 200, message = "북마크 취소 성공")
	@ApiOperation(value = "북마크", notes = "북마크 취소")
	public ResponseEntity<?> delete(@RequestBody BookmarkRequest bookmarkDto) throws Exception {
		bookmarkService.delete(bookmarkDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/feed")
	@ApiResponse(code = 200, message = "북마크 리스트 전달 성공")
	@ApiOperation(value = "포즈 피드", notes = "북마크 리스트 조회")
	public ResponseEntity<List<PoseInfoResponse>> findBookmark(@RequestParam final String uid
		//@RequestParam final Integer pageNumber, @RequestParam final Integer pageSize)
		) {
		//bookmarkService.findBookmark(uid, pageNumber, pageSize);
		return ResponseEntity.ok(bookmarkService.findBookmark(uid));
	}

}
