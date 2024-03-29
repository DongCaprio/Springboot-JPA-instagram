package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //fianl에 대한 생성자를 만들어줌 , DI할때 사용
@Controller // 1. IOC 2.파일을 리턴하는 컨트롤러
public class AuthController {
	
	private final AuthService authService;
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@GetMapping("/auth/signin")
	public String signinForm(SignupDto signupDto) { 
		return "auth/signin";
	}

	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}

	//회원가입버튼 -> /auth/signup -> /auth/signin
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { //key=value (x-ww-form-urIencoded)
		//@ResponseBody 하면 return에 쓰는 String값을 반환함
		//pom.xml에 추가해야지만 @Valid사용가능
		//@Valid 붙은 SignupDto에 오류가 발생하면 그 오류를 전부 BindingResult에 모아줌
		
		//AOP로 유효성 검사 코드가 실행중(파라미터에 BindingResult가 있으므로)
		
			// User <- SignupDto
			User user = signupDto.toEntitity();
			authService.회원가입(user);
			return "auth/signin";
	}
	
	
}
