package com.dndoz.PosePicker.Service;

import java.awt.print.Book;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dndoz.PosePicker.Domain.Bookmark;
import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.User;
import com.dndoz.PosePicker.Dto.BookmarkRequest;
import com.dndoz.PosePicker.Dto.LoginResponse;
import com.dndoz.PosePicker.Repository.BookmarkRepository;
import com.dndoz.PosePicker.Repository.PoseInfoRepository;
import com.dndoz.PosePicker.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService{

	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final PoseInfoRepository poseInfoRepository;

	//북마크 등록
	@Transactional
	public void insert(BookmarkRequest bookmarkDto) throws Exception {

		User user=userRepository.findById(bookmarkDto.getUid()).orElseThrow(NullPointerException::new);
		PoseInfo poseInfo = poseInfoRepository.findByPoseId(bookmarkDto.getPoseId()).orElseThrow(NullPointerException::new);

		//북마크 있으면 북마크 취소
		if (bookmarkRepository.findByUserAndPoseInfo(user, poseInfo).isPresent()){

		}
		Bookmark bookmark = new Bookmark(user, poseInfo);
		bookmarkRepository.save(bookmark);
	}

}
