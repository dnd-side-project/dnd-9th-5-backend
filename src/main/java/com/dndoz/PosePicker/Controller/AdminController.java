package com.dndoz.PosePicker.Controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dndoz.PosePicker.Dto.PoseUpdateRequest;
import com.dndoz.PosePicker.Dto.PoseUploadRequest;
import com.dndoz.PosePicker.Service.AdminService;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	public AdminController(final AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/pose")
	public ResponseEntity<?> uploadData(
		@RequestPart(value = "peopleCount") String peopleCount,
		@RequestPart(value = "frameCount") String frameCount,
		@RequestPart(value = "tags") String tags,
		@RequestPart(value = "source", required = false) String source,
		@RequestPart(value = "sourceUrl", required = false) String sourceUrl,
		@RequestPart(value = "description", required = false) String description,
		@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
		PoseUploadRequest poseUploadRequest = new PoseUploadRequest(peopleCount, frameCount, tags, source, sourceUrl,
			description);
		adminService.uploadPose(poseUploadRequest, multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/pose/{poseId}")
	public ResponseEntity<?> updateData(
		@PathVariable Long poseId,
		@RequestBody PoseUpdateRequest poseDto) throws IOException {
		poseDto.setPoseId(poseId);
		adminService.updatePose(poseDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
