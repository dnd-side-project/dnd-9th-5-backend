package com.dndoz.PosePicker.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dndoz.PosePicker.Dto.BookmarkRequest;
import com.dndoz.PosePicker.Service.BookmarkService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/")
	@ApiResponse(code = 200, message = "북마크 등록 성공")
	@ApiOperation(value = "북마크", notes = "북마크 등록")
	public ResponseEntity<BookmarkRequest> insert(@RequestBody BookmarkRequest bookmarkDto) throws Exception {
		bookmarkService.insert(bookmarkDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	//북마크 취소
}
