package br.com.caelum.viagens.administrativo.support.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.caelum.viagens.administrativo.support.dto.ResourceNotFoundDto;
import br.com.caelum.viagens.administrativo.support.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ResourceNotFoundHandler {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResourceNotFoundDto handler(ResourceNotFoundException exception) {
		return new ResourceNotFoundDto(exception.getMessage());
	}
}
