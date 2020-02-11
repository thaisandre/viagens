package br.com.caelum.viagens.administrativo.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.caelum.viagens.administrativo.handler.dto.FieldErrorDto;
import br.com.caelum.viagens.administrativo.handler.dto.ValidationErrorsDto;

@RestControllerAdvice
public class ValidationErrorsHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ValidationErrorsDto handler(MethodArgumentNotValidException exception) {
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		ValidationErrorsDto validationErrorsDto = new ValidationErrorsDto();
		
		fieldErrors.forEach(error -> {
			validationErrorsDto.addFieldError(new FieldErrorDto(error.getField(), error.getDefaultMessage()));
		});
		
		return validationErrorsDto;
	}
}
