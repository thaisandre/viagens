package br.com.caelum.viagens.administrativo.support;

import java.util.Optional;

import org.springframework.util.Assert;

import br.com.caelum.viagens.administrativo.support.exception.ResourceNotFoundException;

public class IfResourceIsFound {

	private static final String RECURSO_NAO_ENCONTRADO = "Recurso não encontrado";

	public static <T> T of(Optional<T> optional) {
		return of(optional, RECURSO_NAO_ENCONTRADO);
	}

	public static <T> T of(Optional<T> optional, String mensagem) {
		Assert.notNull(optional, "Optional não pode ser nulo");
		Assert.hasLength(mensagem, "A mensagem não pode ser nula ou vazia.");	
		return optional.orElseThrow(() -> new ResourceNotFoundException(mensagem));
	}
}
