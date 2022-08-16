package ai.wapl.noteapi.exception.Exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

//    @ExceptionHandler(CNoteBookNotFoundException.class)
//    @ResponseStatus(HttpStatus.OK)
//    protected CommonResult NotFoundException(HttpServletRequest request, CNoteBookNotFoundException e) {
//        // CommonResult : 응답 결과에 대한 정보
//        return responseService.getFailResult(Integer.valueOf(getMessage("CNoteBookNotFoundException.code")), getMessage("CNoteBookNotFoundException.msg"));
//        // 예외 처리 메시지를 MessageSource에서 가져오도록 수정
//        // getFailResult : setSuccess, setCode, setMsg
//    }
}

