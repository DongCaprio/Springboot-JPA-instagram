package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice //모든 익셉션 다 낙아챈다 (그냥 모든 익셉션 다 받음)
public class ControllerExceptionHandler {

	//handler - ex패키지에 customValidationException만들어줌
	@ExceptionHandler(CustomValidationException.class) // 모든 customValidationException 시 메소드 발동
	public String validationException(CustomValidationException e) { //?자리에 원래 제네릭을 밑에줄과 동일하게 적어야줘되지만 (지금은Map) 그런데 ?를 넣어주면 알아서 들어가게된다
		// CMRespDto, Script비교
		// 1. 클라이언트 응답시에는 Script가 좋음 (클라이언트 응답받을때)
		// Ajax통신 - CMRespDto가 더좋음 (개발자가 응답받을때)
		// Andriod통신 - CMRespDto가 더좋음 (개발자가 응답받을때)
		return Script.back(e.getErrorMap().toString());
	}
	
//	public CMRespDto<?> validationException(CustomValidationException e) { //?자리에 원래 제네릭을 밑에줄과 동일하게 적어야줘되지만 (지금은Map) 그런데 ?를 넣어주면 알아서 들어가게된다
//		return new CMRespDto<Map<String,String>>(-1,e.getMessage(),e.getErrorMap());
//	}
	//위랑비교
}
