package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}") //yml의 file: path 값을 가져온다 (yml다루는 이거 꼭 기억하기)
	private String uploadFolder;
	
	@Transactional(readOnly = true)
	public List<Image>인기사진(){
		List<Image>images = imageRepository.mPopular();
		return images;
	}
	
	@Transactional(readOnly = true)
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// images에 좋아요 상태담기
		//java 람다식 foreach
		//향상된 for문을 foreach(변수 -> {})로 더 간단하게 변환할 수 있다
		images.forEach((image)->{
			image.setLikeCount(image.getLikes().size());
			image.getLikes().forEach((like)->{
				if(like.getUser().getId() == principalId) { // 해당이미지에 좋아요한 사람을 찾아서 현재 로그인한 사람이 좋아요 눌렀는지 비교
					image.setLikeState(true);
				}
			});
		});
		return images;
	}
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); //uuid (uuid란 네트워크상에서 고유성이 보장되는 id를 만들기 위한 표준규약)
		//파일 이름의 중복 방지를 위해서 uuid를 사용한다.
		
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); //실제파일이름 넣기 ex)1.jpg
		System.out.println("이미지 파일이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//try catch 사용하는 이유
		// 통신할때 or I/O(하드디스크에 업로드 or 읽을때) 예외가 발생할 수 있다 예를 들어 1.jpg읽으려고하는데 그 파일이 없는경우 등
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes()); //1. path, 2. imageFile 3. 생략가능
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser()); //43dc3d5a-157d-4a96-bb09-db39a405b3f1_bully.png
		Image imageEntity = imageRepository.save(image);
		//System.out.println(imageEntity); 
	}
}
