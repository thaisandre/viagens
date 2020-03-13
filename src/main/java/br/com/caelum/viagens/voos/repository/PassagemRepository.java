package br.com.caelum.viagens.voos.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.voos.model.Passagem;

public interface PassagemRepository extends CrudRepository<Passagem, Long> {
	
	Optional<Passagem> findByAssentoId(Long assentoId);
}
