package br.com.caelum.viagens.support.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.caelum.viagens.support.dto.FieldErrorDto;
import br.com.caelum.viagens.support.dto.ValidationErrorsDto;

@RestControllerAdvice
public class InvalidFormatHandler {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(InvalidFormatException.class)
	public ValidationErrorsDto handler(InvalidFormatException exception) {
		
		String campo = extraiCampo(Arrays.asList(exception.getPathReference().split("->")));
		String mensagem = "\'" + exception.getValue() + "\' não é um valor válido para " + campo;
		
		ValidationErrorsDto validationErrorsDto = new ValidationErrorsDto();
		validationErrorsDto.addFieldError(new FieldErrorDto(campo, mensagem));

		return validationErrorsDto;
	}
	

	private String extraiCampo(List<String> caminhos) {
		StringBuilder builder = new StringBuilder();
		
		caminhos.forEach(c -> {
			String campo = extraiCampoDoCaminho(c);
			
			if(campo.startsWith("\"")){
				builder.append(campo.replaceAll("\"", "").trim() + ".");
			} else {
				int l = builder.toString().length();
				builder.replace(l-1, l, "[");
				builder.append(campo.trim() + "].");
			}
		});
		
		return builder.substring(0, builder.length()-1);
	}
	
	private String extraiCampoDoCaminho(String caminho) {
		return caminho.subSequence(caminho.indexOf("[")+1, caminho.indexOf("]")).toString();
	}
}
