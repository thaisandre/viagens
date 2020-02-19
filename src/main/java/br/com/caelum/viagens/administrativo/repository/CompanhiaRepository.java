package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.administrativo.model.Companhia;

public interface CompanhiaRepository extends CrudRepository<Companhia, Long>{

	Optional<Companhia> findByNome(String nome);
	
	Optional<Companhia> findById(Long id);

}
