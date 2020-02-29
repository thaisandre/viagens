package br.com.caelum.viagens.voos.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.caelum.viagens.voos.model.TipoParada;
import br.com.caelum.viagens.voos.validator.annotations.TipoParadaExistente;

public class TipoParadaExistenteValidator implements ConstraintValidator<TipoParadaExistente, String> {

	private List<String> tipos;

	public void initialize(TipoParadaExistente constraintAnnotation) {
		tipos = TipoParada.tiposParada();	
	}

	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if(object == null) {
			return true;
		}
		
		if (!tipos.contains(object))
			return false;
		
		return true;
	}
}
