package br.com.caelum.viagens.administrativo.handler.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsDto {

	List<FieldErrorDto> fieldErrors = new ArrayList<>();

	public List<FieldErrorDto> getFieldErrors() {
		return fieldErrors;
	}
	
	public void addFieldError(FieldErrorDto fieldError) {
		this.fieldErrors.add(fieldError);
	}
}
