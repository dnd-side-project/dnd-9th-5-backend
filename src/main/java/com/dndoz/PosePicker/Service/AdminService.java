package com.dndoz.PosePicker.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.PoseTag;
import com.dndoz.PosePicker.Domain.PoseTagAttribute;
import com.dndoz.PosePicker.Dto.PoseInfoResponse;
import com.dndoz.PosePicker.Dto.PoseTalkResponse;
import com.dndoz.PosePicker.Dto.PoseUploadRequest;
import com.dndoz.PosePicker.Dto.PoseUpdateRequest;
import com.dndoz.PosePicker.Repository.PoseFilterRepository;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.PoseTagAttributeRepository;
import com.dndoz.PosePicker.Repository.PoseTagRepository;
import com.dndoz.PosePicker.Repository.PoseTalkRepository;

@Service
public class AdminService {
	private final AmazonS3 amazonS3;
	private final PoseInfoRepository poseInfoRepository;
	private final PoseTalkRepository poseTalkRepository;
	private final PoseTagAttributeRepository poseTagAttributeRepository;
	private final PoseTagRepository poseTagRepository;
	private final PoseFilterRepository poseFilterRepository;

	public AdminService(AmazonS3 amazonS3, final PoseInfoRepository poseInfoRepository, final PoseTalkRepository poseTalkRepository, final PoseTagRepository poseTagRepository,
		final PoseFilterRepository poseFilterRepository, final PoseTagAttributeRepository poseTagAttributeRepository) {
		this.amazonS3 = amazonS3;
		this.poseInfoRepository = poseInfoRepository;
		this.poseTalkRepository = poseTalkRepository;
		this.poseTagRepository = poseTagRepository;
		this.poseFilterRepository = poseFilterRepository;
		this.poseTagAttributeRepository = poseTagAttributeRepository;
	}
	   @Value("${cloud.aws.s3.bucketName}")
	   private String bucketName;

		public String uploadPose(PoseUploadRequest poseDto, MultipartFile multipartFile) throws IOException {
			if (!multipartFile.isEmpty()){
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(multipartFile.getSize());
				metadata.setContentType(multipartFile.getContentType());

				String fileType=(multipartFile.getContentType()).substring(6);  //ex) image/png -> png

				String uploadFileName = poseDto.getFrameCount()+"[pz]"+poseDto.getFrameCount()+"[pz]"+poseDto.getTags()+"[pz]"
					+poseDto.getSource()+"[pz]"+poseDto.getSourceUrl()+"[pz]"+poseDto.getDescription()+".jpg";

				System.out.println(uploadFileName);
				amazonS3.putObject(bucketName, uploadFileName, multipartFile.getInputStream(), metadata);
				return amazonS3.getUrl(bucketName, uploadFileName).toString();
			}
			else return "null";
		}

	//포즈픽(사진) 조회
	public PoseInfoResponse showRandomPoseInfo(Long people_count) {
		PoseInfo poseInfo = poseFilterRepository.findRandomPoseInfo(people_count)
			.orElseThrow(NullPointerException::new);
		return new PoseInfoResponse(bucketName, poseInfo);
	}

	@Transactional
	public String updatePose(PoseUpdateRequest poseDto) {
		// try {
			Optional<PoseInfo> poseInfoOptional = poseInfoRepository.findByPoseId(poseDto.getPoseId());

			if (poseInfoOptional.isPresent()) {
				PoseInfo poseInfo = poseInfoOptional.get();
				// poseInfo.setImageKey(poseDto.getImageKey());
				poseInfo.setPeopleCount(Long.parseLong(poseDto.getPeopleCount()));
				poseInfo.setFrameCount(Long.parseLong(poseDto.getFrameCount()));
				poseInfo.setSource(poseDto.getSource());
				poseInfo.setSourceUrl(poseDto.getSourceUrl());

				poseTagRepository.deleteByPoseId(poseDto.getPoseId());
				// userRepository.deleteById(id);
				String[] tagsArray = poseDto.getTags().split(",");

				for (String tag : tagsArray) {
					PoseTagAttribute tagAttribute = poseTagAttributeRepository.findByPoseTagAttribute(tag);

					if (tagAttribute == null) {
						tagAttribute = new PoseTagAttribute();
						tagAttribute.setAttribute(tag);
						tagAttribute = poseTagAttributeRepository.save(tagAttribute);
					}

					PoseTag poseTag = new PoseTag(tagAttribute, poseInfo);
					poseTagRepository.save(poseTag);
				}
				poseInfoRepository.save(poseInfo);
				return "Pose 업데이트가 성공적으로 완료되었습니다.";
			} else {
				return "Pose 업데이트에 실패했습니다. 지정된 pose_id를 찾을 수 없습니다.";
			}
	}

	public List<PoseTalkResponse> getPoseTalk() {
		return poseTalkRepository.findAllPoseTalk()
			.stream()
			.map(poseTalk -> new PoseTalkResponse(poseTalk))
			.collect(Collectors.toList());

	}
}
