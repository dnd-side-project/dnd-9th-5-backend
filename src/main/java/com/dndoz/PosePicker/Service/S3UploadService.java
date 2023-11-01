package com.dndoz.PosePicker.Service;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;;
import com.dndoz.PosePicker.Dto.ImgUploadRequest;

@Service
@RequiredArgsConstructor
public class S3UploadService {

	   private final AmazonS3Client amazonS3Client;

	   @Value("${cloud.aws.s3.bucketName}")
	   private String bucketName;

	   //s3에 이미지 업로드
		public String uploadFile(ImgUploadRequest imgDto, MultipartFile multipartFile) throws IOException {
			if (!multipartFile.isEmpty()){
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(multipartFile.getSize());
				metadata.setContentType(multipartFile.getContentType());

				String fileType=(multipartFile.getContentType()).substring(6);  //ex) image/png -> png

				String uploadFileName = imgDto.getFrameCount()+"[pz]"+imgDto.getFrameCount()+"[pz]"+imgDto.getTags()+"[pz]"
					+imgDto.getSource()+"[pz]"+imgDto.getSourceUrl()+"[pz]"+imgDto.getDescription()+"."+fileType;

				amazonS3Client.putObject(bucketName, uploadFileName, multipartFile.getInputStream(), metadata);
				return amazonS3Client.getUrl(bucketName, uploadFileName).toString();
			}
			else return "null";
		}

}
