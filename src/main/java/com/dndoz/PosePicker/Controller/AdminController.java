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
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/admin")
@Api(tags = {"이미지 업로드 API"})
@ApiIgnore
public class AdminController {

	private final AdminService uploadService;

	@PostMapping("/imgUpload")
	public ResponseEntity<?> uploadData(
		@RequestPart(value = "peopleCount") String peopleCount,
		@RequestPart(value = "frameCount") String frameCount,
		@RequestPart(value = "tags") String tags,
		@RequestPart(value = "source") String source,
		@RequestPart(value = "sourceUrl") String sourceUrl,
		@RequestPart(value = "description") String description,
		@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
		ImgUploadRequest imgUploadRequest = new ImgUploadRequest(peopleCount, frameCount, tags, source, sourceUrl, description);
		uploadService.uploadFile(imgUploadRequest, multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED).build(		);
	}

}
