package br.com.caelum.viagens.support.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorsDto {

	List<FieldErrorDto> fieldErrors = new ArrayList<>();
	List<String> globalErrors = new ArrayList<>();

	public List<FieldErrorDto> getFieldErrors() {
		return fieldErrors;
	}
	
	public List<String> getGlobalErrors() {
		return globalErrors;
	}
	
	public void addFieldError(FieldErrorDto fieldError) {
		this.fieldErrors.add(fieldError);
	}
	
	public void addGlobalError(String globalError) {
		this.globalErrors.add(globalError);
	}
}
