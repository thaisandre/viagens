package br.com.caelum.viagens.voos.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.caelum.viagens.voos.validator.TipoParadaExistenteValidator;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoParadaExistenteValidator.class)
@Documented
public @interface TipoParadaExistente {

    String message() default "tipo de parada inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
