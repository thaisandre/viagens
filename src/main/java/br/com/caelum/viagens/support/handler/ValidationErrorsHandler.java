package br.com.caelum.viagens.support.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.caelum.viagens.support.dto.FieldErrorDto;
import br.com.caelum.viagens.support.dto.ValidationErrorsDto;

@RestControllerAdvice
public class ValidationErrorsHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ValidationErrorsDto handler(MethodArgumentNotValidException exception) {
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
		
		ValidationErrorsDto validationErrorsDto = new ValidationErrorsDto();
		
		fieldErrors.forEach(error -> {
			validationErrorsDto.addFieldError(new FieldErrorDto(error.getField(), error.getDefaultMessage()));
		});
		
		globalErrors.forEach(error -> {
			validationErrorsDto.addGlobalError(error.getDefaultMessage());
		});
		
		return validationErrorsDto;
	}	
}