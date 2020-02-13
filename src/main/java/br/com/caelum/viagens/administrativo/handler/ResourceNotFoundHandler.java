package br.com.caelum.viagens.administrativo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.caelum.viagens.administrativo.exception.ResourceNotFoundException;
import br.com.caelum.viagens.administrativo.handler.dto.ResourceNotFoundDto;

@RestControllerAdvice
public class ResourceNotFoundHandler {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResourceNotFoundDto handler(ResourceNotFoundException exception) {
		return new ResourceNotFoundDto(exception.getMessage());
	}
}
