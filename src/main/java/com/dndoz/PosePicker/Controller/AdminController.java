package com.dndoz.PosePicker.Controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.dndoz.PosePicker.Dto.ImgUploadRequest;
import com.dndoz.PosePicker.Service.AdminService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/imgUpload")
@Api(tags = {"이미지 업로드 API"})
public class AdminController {

	private final AdminService uploadService;

	@PostMapping("/")
	public ResponseEntity<?> uploadData(
		@RequestPart(value = "imgDto") ImgUploadRequest imgDto,
		@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
